package com.benjamerc.taskmanager.domain.dto.user;

import com.benjamerc.taskmanager.validation.UniqueUsername;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ONLY for PATCH
public class UserPatchDTO {

    @NotNull(message = "Id is required")
    private Long id;

    @Size(max = 255, message = "Username can't be longer than 255 characters")
    @UniqueUsername
    private String username;

    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @Size(max = 255, message = "Email can't be longer than 255 characters")
    private String email;
}
