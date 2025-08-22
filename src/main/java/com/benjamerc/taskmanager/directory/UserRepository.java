package com.benjamerc.taskmanager.directory;

import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByUsername(String username);
}
