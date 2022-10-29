package com.illiapinchukmoodle.controller;

import com.illiapinchukmoodle.data.model.Task;
import com.illiapinchukmoodle.exception.TaskNotFoundException;
import com.illiapinchukmoodle.data.dto.TaskDTO;
import com.illiapinchukmoodle.service.interfacies.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Illia Pinchuk
 */
@RestController
@RequestMapping(path = "/api/tasks", produces = APPLICATION_JSON_VALUE)
public class TaskController {

    private static final Logger logger =
            LoggerFactory.getLogger(TaskController.class);

    private static final String ID = "taskId";
    private static final String NEW_TASK_LOG = "New task was created id: {}";
    private static final String TASK_GET_LOG = "Task with id: {} was found";
    private static final String TASKS_GET_LOG = "Tasks were found";
    private static final String TASK_UPDATED_LOG = "Task: {} was updated";
    private static final String TASK_DELETED_LOG = "Task with id: {} was deleted";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaskService taskService;


    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "Found tasks",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskDTO.class))})
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        logger.info(TASKS_GET_LOG);
        return ResponseEntity.ok(taskService.getAllTasks().stream().map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Crate a new task")
    @ApiResponse(responseCode = "201", description = "Task is created",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskDTO.class))})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        Task taskRequest = modelMapper.map(taskDTO, Task.class);
        Task task = taskService.createTask(taskRequest);
        TaskDTO taskResponse = modelMapper.map(task, TaskDTO.class);

        logger.info(NEW_TASK_LOG, task.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @Operation(summary = "Get task by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found task", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)})
    @GetMapping(path = "/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable(value = ID) Long taskId){
        Task task = taskService.getTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        TaskDTO taskResponse = modelMapper.map(task, TaskDTO.class);

        logger.info(TASK_GET_LOG, taskId);

        return ResponseEntity.ok(taskResponse);
    }

    @Operation(summary = "Update task by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Task was updated", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = TaskDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)})
    @PutMapping(path = "/{taskId}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDTO> updateTask(@Valid @RequestBody TaskDTO taskDTO, @PathVariable(name = ID) Long taskId) {
        Task taskRequest = modelMapper.map(taskDTO, Task.class);
        Task task = taskService.updateTask(taskRequest, taskId);
        TaskDTO taskResponse = modelMapper.map(task, TaskDTO.class);

        logger.info(TASK_UPDATED_LOG, task.toString());

        return ResponseEntity.ok().body(taskResponse);
    }

    @Operation(summary = "Delete task by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Task was deleted", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)})
    @DeleteMapping(path = "/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable(name = ID) Long taskId) {
        taskService.deleteTask(taskId);

        logger.info(TASK_DELETED_LOG, taskId);

        return ResponseEntity.ok("Task was successfully deleted");
    }
}
