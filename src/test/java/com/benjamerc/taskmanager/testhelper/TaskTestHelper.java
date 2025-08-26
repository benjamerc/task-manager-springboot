package com.benjamerc.taskmanager.testhelper;

import com.benjamerc.taskmanager.domain.dto.task.TaskDTO;
import com.benjamerc.taskmanager.domain.dto.task.TaskPostDTO;
import com.benjamerc.taskmanager.util.TestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public record TaskTestHelper(MockMvc mockMvc, ObjectMapper objectMapper) {

    public TaskDTO createTaskViaPost(TaskPostDTO dto) throws Exception {
        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(response, TaskDTO.class);
    }

    public void assertTasksList(TaskDTO... expectedTasks) throws Exception {
        MvcResult result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode root = objectMapper.readTree(json);

        TaskDTO[] tasks = objectMapper.treeToValue(root.get("content"), TaskDTO[].class);

        List<TaskDTO> actualList = TestUtils.sortAndAssertSize(expectedTasks, tasks, TaskDTO::getId);
        List<TaskDTO> expectedList = Arrays.stream(expectedTasks)
                .sorted(Comparator.comparing(TaskDTO::getId))
                .toList();

        for (int i = 0; i < expectedList.size(); i++) {
            TaskDTO expected = expectedList.get(i);
            TaskDTO actual = actualList.get(i);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getTitle(), actual.getTitle());
            assertEquals(expected.getDescription(), actual.getDescription());
            assertEquals(expected.getDueDate(), actual.getDueDate());
            assertEquals(expected.getCompleted(), actual.getCompleted());
            assertEquals(expected.getUserId(), actual.getUserId());
        }
    }


}
