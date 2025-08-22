package com.benjamerc.taskmanager.controller;

import com.benjamerc.taskmanager.domain.dto.task.TaskDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPatchDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPostDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPutDTO;
import com.benjamerc.taskmanager.domain.dto.user.UserDTO;
import com.benjamerc.taskmanager.service.TaskService;
import com.benjamerc.taskmanager.testhelper.TaskTestHelper;
import com.benjamerc.taskmanager.testhelper.UserTestHelper;
import com.benjamerc.taskmanager.util.TaskTestDataUtil;
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
public class TaskControllerIntegrationTests {

    private final TaskService taskService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private UserTestHelper userHelper;

    private TaskTestHelper taskHelper;

    @BeforeEach
    public void setUp() {
        userHelper = new UserTestHelper(mockMvc, objectMapper);
        taskHelper = new TaskTestHelper(mockMvc, objectMapper);
    }

    @Autowired
    public TaskControllerIntegrationTests(TaskService taskService, MockMvc mockMvc,
                                          ObjectMapper objectMapper) {
        this.taskService = taskService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void shouldCreatesTaskAndReturnDto() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());

        TaskPostDTO task = TaskTestDataUtil.createTestTaskPostDTO(user.getId());

        String json = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(task.getDueDate().toString()))
                .andExpect(jsonPath("$.userId").value(task.getUserId()));
    }

    @Test
    public void shouldReturnBadRequestWhenCreatingTaskWithNonExistingUserId() throws Exception {
        TaskPostDTO task = TaskTestDataUtil.createTestTaskPostDTO(99L);

        String json = objectMapper.writeValueAsString(task);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnAllTasksDto() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        TaskDTO task1 = taskHelper.createTaskViaPost(TaskTestDataUtil.createTestTaskPostDTO(user.getId()));
        TaskDTO task2 = taskHelper.createTaskViaPost(TaskTestDataUtil.createTestTaskPostDTO(user.getId()));

        taskHelper.assertTasksList(task1, task2);
    }

    @Test
    public void shouldReturnsTaskById() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        TaskDTO task = taskHelper.createTaskViaPost(TaskTestDataUtil.createTestTaskPostDTO(user.getId()));

        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(task.getDueDate().toString()))
                .andExpect(jsonPath("$.userId").value(task.getUserId()));
    }

    @Test
    public void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFullUpdatesTaskAndReturnsDto() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        TaskDTO task = taskHelper.createTaskViaPost(TaskTestDataUtil.createTestTaskPostDTO(user.getId()));

        TaskPutDTO fullUpdate = TaskTestDataUtil.createTestTaskPutDTO(task.getId());

        String json = objectMapper.writeValueAsString(fullUpdate);

        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(fullUpdate.getTitle()))
                .andExpect(jsonPath("$.description").value(fullUpdate.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(fullUpdate.getDueDate().toString()));
    }

    @Test
    public void shouldReturnBadRequestWhenFullUpdatedTaskWithBodyAndPathIdDoesNotMatch() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        TaskDTO task = taskHelper.createTaskViaPost(TaskTestDataUtil.createTestTaskPostDTO(user.getId()));

        TaskPutDTO fullUpdate = TaskTestDataUtil.createTestTaskPutDTO(user.getId());
        fullUpdate.setId(99L);

        String json = objectMapper.writeValueAsString(fullUpdate);

        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldPartialUpdatesTaskAndReturnsDto() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        TaskDTO task = taskHelper.createTaskViaPost(TaskTestDataUtil.createTestTaskPostDTO(user.getId()));

        TaskPatchDTO partialUpdate = TaskTestDataUtil.createTestTaskPatchDTO(task.getId());

        String json = objectMapper.writeValueAsString(partialUpdate);

        mockMvc.perform(patch("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(partialUpdate.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.dueDate").value(task.getDueDate().toString()))
                .andExpect(jsonPath("$.completed").value(partialUpdate.getCompleted()))
                .andExpect(jsonPath("$.userId").value(task.getUserId()));
    }

    @Test
    public void shouldReturnNotFoundWhenPartialUpdatingNonExistingTask() throws Exception {
        TaskPatchDTO partialUpdate = TaskTestDataUtil.createTestTaskPatchDTO(99L);

        String json = objectMapper.writeValueAsString(partialUpdate);

        mockMvc.perform(patch("/tasks/{id}", partialUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteTaskById() throws Exception {
        UserDTO user = userHelper.createUserViaPost(UserTestDataUtil.createTestUserPostDTOA());
        TaskDTO task = taskHelper.createTaskViaPost(TaskTestDataUtil.createTestTaskPostDTO(user.getId()));

        mockMvc.perform(delete("/tasks/{id}", task.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingTaskNonExists() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
