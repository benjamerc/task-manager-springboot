package com.benjamerc.taskmanager.directory;

import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
