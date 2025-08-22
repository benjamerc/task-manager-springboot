package com.benjamerc.taskmanager.controller;

import com.benjamerc.taskmanager.domain.dto.user.UserDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPatchDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPostDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserPutDTO;
import com.benjamerc.taskmanager.service.UserService;
import com.benjamerc.taskmanager.testhelper.UserTestHelper;
import com.benjamerc.taskmanager.util.UserTestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerIntegrationTests {

    private MockMvc mockMvc;

    private UserService userService;

    private ObjectMapper objectMapper;

    private UserTestHelper userHelper;

    @BeforeEach
    public void setUp() {
        userHelper = new UserTestHelper(mockMvc, objectMapper);
    }

    @Autowired
    public UserControllerIntegrationTests(
            MockMvc mockMvc, UserService userService, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void shouldCreateUserAndReturnUserDto() throws Exception {
        UserPostDTO user = UserTestDataUtil.createTestUserPostDTOA();

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void shouldReturnsAllUsersInDatabase() throws Exception {
        UserDTO user1 = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        UserDTO user2 = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOB());

        userHelper.assertUsersList(user1, user2);
    }

    @Test
    public void shouldReturnsUserInDatabaseById() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void shouldFullUpdatesUser() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        UserPutDTO fullUpdate = UserTestDataUtil.createTestUserPutDTO(user.getId());

        fullUpdate.setUsername("Updated");

        String jsonUpdate = objectMapper.writeValueAsString(fullUpdate);

        mockMvc.perform(put("/users/{id}", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(fullUpdate.getUsername()))
                .andExpect(jsonPath("$.email").value(fullUpdate.getEmail()));
    }

    @Test
    public void shouldReturn400WhenPathIdAndBodyIdDoNotMatchOnFullUpdate() throws Exception {
        Long pathId = 1L;
        Long bodyId = 2L;

        UserPutDTO fullUpdate = UserTestDataUtil.createTestUserPutDTO(bodyId);

        String jsonUpdate = objectMapper.writeValueAsString(fullUpdate);

        mockMvc.perform(put("/users/{id}", pathId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("ID in path and body do not match"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void shouldPartialUpdatesUser() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        UserPatchDTO partialUpdate = UserTestDataUtil.createTestUserPatchDTO(user.getId());

        String jsonUpdate = objectMapper.writeValueAsString(partialUpdate);

        mockMvc.perform(patch("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(partialUpdate.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void shouldReturn400WhenPathIdAndBodyIdDoNotMatchOnPartialUpdate() throws Exception {
        Long pathId = 1L;
        Long bodyId = 2L;

        UserPatchDTO partialUpdate = UserTestDataUtil.createTestUserPatchDTO(bodyId);

        String jsonUpdate = objectMapper.writeValueAsString(partialUpdate);

        mockMvc.perform(patch("/users/{id}", pathId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("ID in path and body do not match"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void shouldReturn404WhenUserNotFoundOnPartialUpdate() throws Exception {
        UserPatchDTO partialUpdate = UserTestDataUtil.createTestUserPatchDTO(99L);

        String jsonUpdate = objectMapper.writeValueAsString(partialUpdate);

        mockMvc.perform(patch("/users/{id}", partialUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: " + partialUpdate.getId()))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void shouldDeletesUser() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());

        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn404WhenUserNotFoundOnDelete() throws Exception {
        mockMvc.perform(delete("/users/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
