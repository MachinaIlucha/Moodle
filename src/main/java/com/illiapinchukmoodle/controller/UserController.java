package com.illiapinchukmoodle.controller;

import com.illiapinchukmoodle.data.dto.AdminUserDTO;
import com.illiapinchukmoodle.data.dto.CourseDTO;
import com.illiapinchukmoodle.data.dto.UserDTO;
import com.illiapinchukmoodle.exception.UserNotFoundException;
import com.illiapinchukmoodle.data.model.User;
import com.illiapinchukmoodle.service.interfacies.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Illia Pinchuk
 */
@RestController
@RequestMapping(path = "/api/users", produces = APPLICATION_JSON_VALUE)
@Slf4j
public class UserController {

    private static final String ID = "userId";
    private static final String NEW_USER_LOG = "New user was created id: {}";
    private static final String USER_GET_LOG = "User with id: {} was found";
    private static final String USERS_GET_LOG = "Users were found";
    private static final String USER_UPDATED_LOG = "User: {} was updated";
    private static final String USER_DELETED_LOG = "User with id: {} was deleted";
    private static final String USER_GET_COURSES_LOG = "Courses of user with id: {} were found";

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    @Operation(summary = "Get user courses")
    @ApiResponse(responseCode = "200", description = "Found courses of user",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = CourseDTO.class)))})
    @Secured({ "ROLE_STUDENT", "ROLE_TEACHER","ROLE_ADMIN" })
    @GetMapping(path = "/{userId}/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesOfUser(@PathVariable(name = ID) Long userId) {
        User user = userService.getUserWithCourses(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<CourseDTO> userCourses = user.getUserCourses().stream()
                .map(course -> modelMapper.map(course, CourseDTO.class)).toList();

        log.info(USER_GET_COURSES_LOG, userId);

        return ResponseEntity.ok(userCourses);
    }

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Found users",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AdminUserDTO.class)))})
    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        log.info(USERS_GET_LOG);
        return ResponseEntity.ok(userService.getAllUsers().stream().map(user -> modelMapper.map(user, AdminUserDTO.class))
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Crate a new user")
    @ApiResponse(responseCode = "201", description = "User is created",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class))})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User userRequest = modelMapper.map(userDTO, User.class);
        User user = userService.createUser(userRequest);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);

        log.info(NEW_USER_LOG, user.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @Operation(summary = "Get user by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found user", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdminUserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/{userId}")
    public ResponseEntity<AdminUserDTO> getUserById(@PathVariable(value = ID) Long userId){
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        AdminUserDTO userResponse = modelMapper.map(user, AdminUserDTO.class);

        log.info(USER_GET_LOG, userId);

        return ResponseEntity.ok().body(userResponse);
    }

    @Operation(summary = "Update user by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User was updated", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @Secured({ "ROLE_STUDENT", "ROLE_TEACHER","ROLE_ADMIN" })
    @PutMapping(path = "/{userId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable(name = ID) Long userId) {
        User userRequest = modelMapper.map(userDTO, User.class);
        User user = userService.updateUser(userRequest, userId);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);

        log.info(USER_UPDATED_LOG, user.toString());

        return ResponseEntity.ok().body(userResponse);
    }

    @Operation(summary = "Delete user by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User was deleted", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AdminUserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @Secured("ROLE_ADMIN")
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<AdminUserDTO> deleteUser(@PathVariable(name = ID) Long userId) {
        User user = userService.deleteUser(userId);
        AdminUserDTO userResponse = modelMapper.map(user, AdminUserDTO.class);

        log.info(USER_DELETED_LOG, userId);

        return ResponseEntity.ok().body(userResponse);
    }
}
