package com.illiapinchukmoodle.controller;

import com.illiapinchukmoodle.data.model.TaskProgress;
import com.illiapinchukmoodle.data.dto.TaskProgressDTO;
import com.illiapinchukmoodle.service.interfacies.TaskProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Illia Pinchuk
 */
@RestController
@RequestMapping(path = "/api/taskProgresses", produces = APPLICATION_JSON_VALUE)
public class TaskProgressController {

    private static final Logger logger =
            LoggerFactory.getLogger(TaskProgressController.class);

    private static final String ID = "taskProgressId";
    private static final String TASKSPROGRESSES_GET_LOG = "Tasks progresses were found";
    private static final String TASKPROGRESS_UPDATED_LOG = "Task progress: {} was updated";

    @Autowired
    private TaskProgressService taskProgressService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Get all tasks progresses by user id")
    @ApiResponse(responseCode = "200", description = "Found tasks progresses by user",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = TaskProgressDTO.class)))})
    @GetMapping(path ="/{userId}")
    public ResponseEntity<List<TaskProgressDTO>> getAllTasksProgressesByUser(@PathVariable(name = "userId") Long userId) {
        List<TaskProgress> taskProgressesByUser = taskProgressService.getAllTaskProgressForUserId(userId);

        logger.info(TASKSPROGRESSES_GET_LOG);

        List<TaskProgressDTO> taskProgressesDto = taskProgressesByUser.stream()
                .map(taskProgress -> modelMapper.map(taskProgress, TaskProgressDTO.class)).toList();
        return ResponseEntity.ok(taskProgressesDto);
    }

    @Operation(summary = "Get all done tasks progresses by user id")
    @ApiResponse(responseCode = "200", description = "Found done tasks progresses by user",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = TaskProgressDTO.class)))})
    @GetMapping(path ="/{userId}/done")
    public ResponseEntity<List<TaskProgressDTO>> getAllVerifiedTasksByUser(@PathVariable(name = "userId") Long userId) {
        List<TaskProgress> verifiedTasksByUser = taskProgressService.getAllTaskProgressForUserId(userId)
                .stream().filter(TaskProgress::isVerified).toList();

        logger.info(TASKSPROGRESSES_GET_LOG);

        List<TaskProgressDTO> taskProgressesDto = verifiedTasksByUser.stream()
                .map(taskProgress -> modelMapper.map(taskProgress, TaskProgressDTO.class)).toList();
        return ResponseEntity.ok(taskProgressesDto);
    }

    @Operation(summary = "Update task progress by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Task progress was updated", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskProgressDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Task progress not found", content = @Content)})
    @PutMapping(path = "/{taskProgressId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskProgressDTO> updateTaskProgress(@Valid  @RequestBody TaskProgressDTO taskProgressDTO,
                                                              @PathVariable(name = ID) Long taskProgressId) {
        TaskProgress taskProgressRequest = modelMapper.map(taskProgressDTO, TaskProgress.class);
        TaskProgress taskProgress = taskProgressService.updateTaskProgress(taskProgressRequest, taskProgressId);
        TaskProgressDTO taskProgressResponse = modelMapper.map(taskProgress, TaskProgressDTO.class);

        logger.info(TASKPROGRESS_UPDATED_LOG, taskProgress.toString());

        return ResponseEntity.ok().body(taskProgressResponse);
    }
}
