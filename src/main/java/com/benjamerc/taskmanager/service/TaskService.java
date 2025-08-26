package com.benjamerc.taskmanager.service;

import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskEntity save(TaskEntity task);

    Page<TaskEntity> findAll(Pageable pageable);

    TaskEntity findById(Long id);

    TaskEntity fullUpdate(Long id, TaskEntity task);

    TaskEntity partialUpdate(Long id, TaskEntity task);

    void deleteById(Long id);

    void deleteAll();
}
