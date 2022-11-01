package com.illiapinchukmoodle.controller;

import com.illiapinchukmoodle.data.dto.AdminCourseDTO;
import com.illiapinchukmoodle.data.dto.CourseDTO;
import com.illiapinchukmoodle.data.dto.UserDTO;
import com.illiapinchukmoodle.exception.CourseNotFoundException;
import com.illiapinchukmoodle.exception.UserNotFoundException;
import com.illiapinchukmoodle.data.model.Course;
import com.illiapinchukmoodle.service.interfacies.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Illia Pinchuk
 */
@RestController
@RequestMapping(path = "/api/courses", produces = APPLICATION_JSON_VALUE)
@Slf4j
public class CourseController {
    private static final String ID = "courseId";
    private static final String NEW_COURSE_LOG = "New course was created id: {}";
    private static final String COURSE_GET_LOG = "Course with id: {} was found";
    private static final String COURSES_GET_LOG = "Courses were found";
    private static final String COURSE_UPDATED_LOG = "Course: {} was updated";
    private static final String COURSE_DELETED_LOG = "Course with id: {} was deleted";
    private static final String COURSE_DELETED_FROM_USER_LOG = "Course with id: {} was deleted from user with id: {}";
    private static final String COURSE_ADD_TO_USER_LOG = "Course with id: {} was added to user with id: {}";
    private static final String COURSE_GET_PROGRESS_OF_USER_LOG = "Progress in percentage of course with id: {} was calculated to user with id: {}";
    private static final String COURSE_GET_USERS_LOG = "Users of course with id: {} were found";


    @Autowired
    private CourseService courseService;
    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get course progress of user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Course progress was calculated",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Course or user were not found", content = @Content)})
    @Secured({"ROLE_STUDENT", "ROLE_TEACHER", "ROLE_ADMIN"})
    @GetMapping(path = "/{courseId}/{userId}")
    public ResponseEntity<Integer> getCourseProgressByUser(@PathVariable(name = ID) Long courseId,
                                                           @PathVariable(name = "userId") Long userId){
        Integer res = courseService.getCourseProgressByUser(courseId, userId);

        log.info(COURSE_GET_PROGRESS_OF_USER_LOG, courseId, userId);

        return ResponseEntity.ok(res);
    }


    @Operation(summary = "Add course to user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Course was added",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdminCourseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Course or user were not found", content = @Content)})
    @Secured("ROLE_ADMIN")
    @PutMapping(path = "/{courseId}/{userId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCourseDTO> addCourseToUser(@PathVariable(name = "userId") Long userId, @PathVariable(name = ID) Long courseId) {
        Course course = courseService.addCourseToUser(courseId, userId);
        AdminCourseDTO courseResponse = modelMapper.map(course, AdminCourseDTO.class);

        log.info(COURSE_ADD_TO_USER_LOG, courseId, userId);

        return ResponseEntity.ok().body(courseResponse);
    }

    @Operation(summary = "Delete course from user")
    @ApiResponse(responseCode = "200", description = "Course was deleted",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = String.class)))})
    @Secured("ROLE_ADMIN")
    @DeleteMapping(path = "/{courseId}/{userId}")
    public ResponseEntity<String> deleteCourseFromUser(@PathVariable(name = ID) Long courseId,
                                                       @PathVariable(name = "userId") Long userId) {
        courseService.deleteCourseFromUser(courseId, userId);

        log.info(COURSE_DELETED_FROM_USER_LOG, courseId, userId);

        return ResponseEntity.ok("Course was successfully deleted from user");
    }

    @Operation(summary = "Get users of course")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Got course users",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Course was not found", content = @Content)})
    @Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
    @GetMapping(path = "/{courseId}/users")
    public ResponseEntity<List<UserDTO>> getCourseUsers(@PathVariable(name = ID) Long courseId){
        Course course = courseService.getCourseWithUsers(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        List<UserDTO> courseUsers = course.getCourseUsers().stream()
                .map(user -> modelMapper.map(user, UserDTO.class)).toList();

        log.info(COURSE_GET_USERS_LOG, courseId);

        return ResponseEntity.ok(courseUsers);
    }

    @Operation(summary = "Get all courses")
    @ApiResponse(responseCode = "200", description = "Found courses",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AdminCourseDTO.class)))})
    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<AdminCourseDTO>> getAllCourses() {
        log.info(COURSES_GET_LOG);
        return ResponseEntity.ok(courseService.getAllCourses().stream()
                .map(course -> modelMapper.map(course, AdminCourseDTO.class))
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Crate a new course")
    @ApiResponse(responseCode = "201", description = "Course is created",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdminCourseDTO.class))})
    @Secured("ROLE_ADMIN")
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        Course courseRequest = modelMapper.map(courseDTO, Course.class);
        Course course = courseService.createCourse(courseRequest);
        AdminCourseDTO courseResponse = modelMapper.map(course, AdminCourseDTO.class);

        log.info(NEW_COURSE_LOG, course.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(courseResponse);
    }

    @Operation(summary = "Get course by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found course",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdminCourseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)})
    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/{courseId}")
    public ResponseEntity<AdminCourseDTO> getCourseById(@PathVariable(name = ID) Long courseId){
        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new UserNotFoundException(courseId));
        AdminCourseDTO courseResponse = modelMapper.map(course, AdminCourseDTO.class);

        log.info(COURSE_GET_LOG, courseId);

        return ResponseEntity.ok(courseResponse);
    }

    @Operation(summary = "Update course by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Course was updated",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CourseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)})
    @Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
    @PutMapping(path = "/{courseId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> updateCourse(@Valid @RequestBody CourseDTO courseDTO, @PathVariable(name = ID) Long courseId) {
        Course courseRequest = modelMapper.map(courseDTO, Course.class);
        Course course = courseService.updateCourse(courseRequest, courseId);
        CourseDTO courseResponse = modelMapper.map(course, CourseDTO.class);

        log.info(COURSE_UPDATED_LOG, course.toString());

        return ResponseEntity.ok().body(courseResponse);
    }

    @Operation(summary = "Delete course by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Course was deleted",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdminCourseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)})
    @Secured("ROLE_ADMIN")
    @DeleteMapping(path = "/{courseId}")
    public ResponseEntity<AdminCourseDTO> deleteCourse(@PathVariable(name = ID) Long courseId) {
        Course course = courseService.deleteCourse(courseId);
        AdminCourseDTO courseResponse = modelMapper.map(course, AdminCourseDTO.class);

        log.info(COURSE_DELETED_LOG, courseId);

        return ResponseEntity.ok().body(courseResponse);
    }
}
