package com.benjamerc.taskmanager.directory;

import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
}
