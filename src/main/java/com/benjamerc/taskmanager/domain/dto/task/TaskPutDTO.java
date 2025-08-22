package com.benjamerc.taskmanager.domain.dto.task;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
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
// ONLY for PUT
public class TaskPutDTO {

    @NotNull(message = "Id is required")
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title can't be longer than 255 characters")
    private String title;

    @Size(max = 255, message = "Description can't be longer than 255 characters")
    private String description;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;

    @NotNull(message = "Completed status is required")
    private Boolean completed;
}
