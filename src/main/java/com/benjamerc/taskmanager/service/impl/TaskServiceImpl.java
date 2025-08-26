package com.benjamerc.taskmanager.service.impl;

import com.benjamerc.taskmanager.directory.TaskRepository;
import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;
import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import com.benjamerc.taskmanager.exception.task.TaskIdMismatchException;
import com.benjamerc.taskmanager.exception.task.TaskNotFoundException;
import com.benjamerc.taskmanager.service.TaskService;
import com.benjamerc.taskmanager.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    private UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public TaskEntity save(TaskEntity task) {
        UserEntity user = userService.findById(task.getUser().getId());
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Override
    public Page<TaskEntity> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }


    @Override
    public TaskEntity findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    @Override
    public TaskEntity fullUpdate(Long id, TaskEntity task) {
        if (!id.equals(task.getId())) {
            throw new TaskIdMismatchException("ID in path and body do not match");
        }

        TaskEntity originalTask = findById(id);

        originalTask.setTitle(task.getTitle());
        originalTask.setDescription(task.getDescription());
        originalTask.setDueDate(task.getDueDate());
        originalTask.setDueDate(task.getDueDate());

        return taskRepository.save(originalTask);
    }

    @Override
    public TaskEntity partialUpdate(Long id, TaskEntity task) {
        if (!id.equals(task.getId())) {
            throw new TaskIdMismatchException("ID in path and body do not match");
        }

        return taskRepository.findById(id).map(existingTask -> {
            Optional.ofNullable(task.getTitle()).ifPresent(existingTask::setTitle);
            Optional.ofNullable(task.getDescription()).ifPresent(existingTask::setDescription);
            Optional.ofNullable(task.getDueDate()).ifPresent(existingTask::setDueDate);
            Optional.ofNullable(task.getCompleted()).ifPresent(existingTask::setCompleted);
            return taskRepository.save(existingTask);
        }).orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        taskRepository.deleteAll();
    }
}
