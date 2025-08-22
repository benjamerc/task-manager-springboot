package com.benjamerc.taskmanager.mapper;

import com.benjamerc.taskmanager.domain.dto.task.TaskDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPatchDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPostDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPutDTO;
import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;
import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public TaskEntity fromPostDto(TaskPostDTO dto) {
        return TaskEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .completed(false)
                .user(UserEntity.builder().id(dto.getUserId()).build())
                .build();
    }

    public TaskEntity fromPutDto(TaskPutDTO dto) {
        return modelMapper.map(dto, TaskEntity.class);
    }

    public TaskEntity fromPatchDto(TaskPatchDTO dto, TaskEntity existingEntity) {
        modelMapper.map(dto, existingEntity);
        return existingEntity;
    }

    public TaskEntity fromPatchDto(TaskPatchDTO dto) {
        TaskEntity task = new TaskEntity();
        modelMapper.map(dto, task);
        return task;
    }

    public TaskDTO toDto(TaskEntity entity) {
        return modelMapper.map(entity, TaskDTO.class);
    }
}
