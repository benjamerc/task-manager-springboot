package com.benjamerc.taskmanager.service.impl;

import com.benjamerc.taskmanager.directory.UserRepository;
import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import com.benjamerc.taskmanager.exception.user.UserIdMismatchException;
import com.benjamerc.taskmanager.exception.user.UserNotFoundException;
import com.benjamerc.taskmanager.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> findAll() {
        Iterable<UserEntity> userEntities = userRepository.findAll();
        return StreamSupport.stream(userEntities.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserEntity fullUpdate(Long id, UserEntity user) {
        if (!id.equals(user.getId())) {
            throw new UserIdMismatchException("ID in path and body do not match");
        }

        UserEntity existingUser = findById(id);

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());

        return userRepository.save(existingUser);
    }

    @Override
    public UserEntity partialUpdate(Long id, UserEntity user) {
        if (!id.equals(user.getId())) {
            throw new UserIdMismatchException("ID in path and body do not match");
        }

        return userRepository.findById(id).map(existingUser -> {
            Optional.ofNullable(user.getUsername()).ifPresent(existingUser::setUsername);
            Optional.ofNullable(user.getPassword()).ifPresent(existingUser::setPassword);
            Optional.ofNullable(user.getEmail()).ifPresent(existingUser::setEmail);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
