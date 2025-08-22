package com.benjamerc.taskmanager.mapper;

import com.benjamerc.taskmanager.domain.dto.user.UserDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPatchDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPostDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPutDTO;
import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public UserEntity fromPostDto(UserPostDTO dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserEntity fromPutDto(UserPutDTO dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserDTO toDto(UserEntity entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    public UserEntity fromPatchDto(UserPatchDTO dto, UserEntity existingEntity) {
        modelMapper.map(dto, existingEntity);
        return existingEntity;
    }

    public UserEntity fromPatchDto(UserPatchDTO dto) {
        UserEntity user = new UserEntity();
        modelMapper.map(dto, user);
        return user;
    }
}
