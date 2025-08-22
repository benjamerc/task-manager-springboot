package com.benjamerc.taskmanager.service;

import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;

import java.util.List;

public interface TaskService {

    TaskEntity save(TaskEntity task);

    List<TaskEntity> findAll();

    TaskEntity findById(Long id);

    TaskEntity fullUpdate(Long id, TaskEntity task);

    TaskEntity partialUpdate(Long id, TaskEntity task);

    void deleteById(Long id);

    void deleteAll();
}
