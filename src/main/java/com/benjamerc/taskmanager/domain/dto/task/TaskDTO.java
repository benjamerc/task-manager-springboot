package com.benjamerc.taskmanager.domain.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ONLY for GET
public class TaskDTO {

    private Long id;

    private String title;

    private String description;

    private LocalDate dueDate;

    private Boolean completed;

    private Long userId;
}
