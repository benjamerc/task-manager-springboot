package com.benjamerc.taskmanager.domain.dto.user;

import com.benjamerc.taskmanager.validation.UniqueUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ONLY for POST
public class UserPostDTO {

    @NotBlank(message = "Username is mandatory")
    @UniqueUsername
    @Size(max = 255, message = "Username can't be longer than 255 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @Size(max = 255)
    private String email;
}
