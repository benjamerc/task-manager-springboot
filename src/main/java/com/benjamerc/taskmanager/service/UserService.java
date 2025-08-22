package com.benjamerc.taskmanager.service;

import com.benjamerc.taskmanager.domain.entitiy.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity save(UserEntity user);

    List<UserEntity> findAll();

    UserEntity findById(Long id);

    UserEntity fullUpdate(Long id, UserEntity user);

    UserEntity partialUpdate(Long id, UserEntity user);

    void deleteById(Long id);

    boolean existsById(Long userId);

    void deleteAll();
}
