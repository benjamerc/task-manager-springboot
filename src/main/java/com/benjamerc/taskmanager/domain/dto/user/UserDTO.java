package com.benjamerc.taskmanager.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ONLY for GET
public class UserDTO {

    private Long id;

    private String username;

    private String email;
}
