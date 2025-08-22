package com.benjamerc.taskmanager.util;

import com.benjamerc.taskmanager.domain.dto.user.UserDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPatchDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPostDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPutDTO;
import com.benjamerc.taskmanager.domain.entitiy.UserEntity;

public class UserTestDataUtil {

    public static UserEntity createTestUserEntity() {
        return UserEntity.builder()
                .username("Manolo")
                .password("12345pass")
                .email("manolo@email.com")
                .build();
    }

    public static UserDTO createTestUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .username("Manolo")
                .email("manolo@email.com")
                .build();
    }

    public static UserPatchDTO createTestUserPatchDTO(Long existingUserId) {
        return UserPatchDTO.builder()
                .id(existingUserId)
                .username("ManoloPatch")
                .password(null)
                .email(null)
                .build();
    }

    public static UserPostDTO createTestUserPostDTOA() {
        return UserPostDTO.builder()
                .username("ManoloPost")
                .password("12345pass")
                .email("manolo.post@email.com")
                .build();
    }

    public static UserPostDTO createTestUserPostDTOB() {
        return UserPostDTO.builder()
                .username("DarkPerson")
                .password("12345pass")
                .email("darkperson.post@email.com")
                .build();
    }

    public static UserPutDTO createTestUserPutDTO(Long existingUserId) {
        return UserPutDTO.builder()
                .id(existingUserId)
                .username("ManoloPost")
                .password("12345pass")
                .email(null)
                .build();
    }
}
