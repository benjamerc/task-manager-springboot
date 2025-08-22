package com.benjamerc.taskmanager.util;

import com.benjamerc.taskmanager.domain.dto.task.TaskDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPatchDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPostDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPutDTO;
import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;

import java.time.LocalDate;

public class TaskTestDataUtil {

    public static TaskEntity createTestTaskEntity() {
        return TaskEntity.builder()
                .title("Title Entity Task")
                .description("Description Entity Task")
                .dueDate(LocalDate.of(2025, 12, 31))
                .user(UserTestDataUtil.createTestUserEntity())
                .build();
    }

    public static TaskDTO createTestTaskDTO(Long userId) {
        return TaskDTO.builder()
                .id(1L)
                .title("Title DTO Task")
                .description("Description DTO Task")
                .dueDate(LocalDate.of(2025, 12, 31))
                .completed(false)
                .userId(userId)
                .build();
    }

    public static TaskPatchDTO createTestTaskPatchDTO(Long existingTaskId) {
        return TaskPatchDTO.builder()
                .id(existingTaskId)
                .title("Updated Title")
                .completed(true)
                .build();
    }

    public static TaskPostDTO createTestTaskPostDTO(Long userId) {
        return TaskPostDTO.builder()
                .title("Title PostDTO Task")
                .description("Description PostDTO Task")
                .dueDate(LocalDate.of(2025, 12, 31))
                .userId(userId)
                .build();
    }

    public static TaskPutDTO createTestTaskPutDTO(Long existingTaskId) {
        return TaskPutDTO.builder()
                .id(existingTaskId)
                .title("Updated Title")
                .dueDate(LocalDate.of(2025, 12, 31))
                .completed(true)
                .build();
    }
}
