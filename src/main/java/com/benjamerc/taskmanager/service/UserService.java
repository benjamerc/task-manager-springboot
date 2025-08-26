package com.benjamerc.taskmanager.service;

import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserEntity save(UserEntity user);

    Page<UserEntity> findAll(Pageable pageable);

    UserEntity findById(Long id);

    UserEntity fullUpdate(Long id, UserEntity user);

    UserEntity partialUpdate(Long id, UserEntity user);

    void deleteById(Long id);

    boolean existsById(Long userId);

    void deleteAll();
}
