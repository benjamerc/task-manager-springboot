package com.benjamerc.taskmanager.controller;

import com.benjamerc.taskmanager.domain.dto.task.TaskDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPatchDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPostDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPutDTO;
import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;
import com.benjamerc.taskmanager.mapper.TaskMapper;
import com.benjamerc.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskPostDTO taskDTO) {
        TaskEntity savedTask = taskService.save(taskMapper.fromPostDto(taskDTO));

        return ResponseEntity
                .created(URI.create("/tasks/" + savedTask.getId()))
                .body(taskMapper.toDto(savedTask));
    }

    @GetMapping
    public ResponseEntity<Page<TaskDTO>> getAllTasks(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable ) {

        Page<TaskDTO> taskDTOS = taskService.findAll(pageable)
                .map(taskMapper::toDto);

        return ResponseEntity.ok(taskDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") Long id) {
        TaskDTO taskDTO = taskMapper.toDto(taskService.findById(id));
        return ResponseEntity.ok(taskDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> fullUpdateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskPutDTO updatedTask) {
        TaskEntity fullUpdate = taskService.fullUpdate(id, taskMapper.fromPutDto(updatedTask));
        return ResponseEntity.ok(taskMapper.toDto(fullUpdate));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskDTO> partialUpdateTask(@PathVariable("id") Long id, @Valid @RequestBody TaskPatchDTO updatedTask) {
        TaskEntity patchEntity = taskMapper.fromPatchDto(updatedTask);
        TaskEntity savedEntity = taskService.partialUpdate(id, patchEntity);

        return ResponseEntity.ok(taskMapper.toDto(savedEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
