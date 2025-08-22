package com.benjamerc.taskmanager.domain.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ONLY for PATCH
public class TaskPatchDTO {

    @NotNull(message = "Id is required")
    private Long id;

    @Size(max = 255, message = "Title can't be longer than 255 characters")
    private String title;

    @Size(max = 255, message = "Description can't be longer than 255 characters")
    private String description;

    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    private Boolean completed;
}
