package com.benjamerc.taskmanager.testhelper;

import com.benjamerc.taskmanager.domain.dto.user.UserDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPostDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public record UserTestHelper(MockMvc mockMvc, ObjectMapper objectMapper) {

    public UserDTO createUserViaPost(UserPostDTO dto) throws Exception {
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(response, UserDTO.class);
    }

    public void assertUsersList(UserDTO... expectedUsers) throws Exception {
        MvcResult result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        UserDTO[] users = objectMapper.readValue(json, UserDTO[].class);

        assertEquals(expectedUsers.length, users.length);

        for (int i = 0; i < expectedUsers.length; i++) {
            assertEquals(expectedUsers[i].getId(), users[i].getId());
            assertEquals(expectedUsers[i].getUsername(), users[i].getUsername());
            assertEquals(expectedUsers[i].getEmail(), users[i].getEmail());
        }
    }
}
