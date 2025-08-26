package com.benjamerc.taskmanager.service;

import com.benjamerc.taskmanager.directory.TaskRepository;
import com.benjamerc.taskmanager.domain.entitiy.TaskEntity;
import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import com.benjamerc.taskmanager.exception.task.TaskNotFoundException;
import com.benjamerc.taskmanager.service.impl.TaskServiceImpl;
import com.benjamerc.taskmanager.util.TaskTestDataUtil;
import com.benjamerc.taskmanager.util.UserTestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.benjamerc.taskmanager.util.TaskTestDataUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void testThatShouldSaveTaskSuccessfully() {
        TaskEntity taskEntity = TaskTestDataUtil.createTestTaskEntity();
        TaskEntity savedTaskEntity = TaskTestDataUtil.createTestTaskEntity();
        savedTaskEntity.setId(1L);

        UserEntity testUser = taskEntity.getUser();

        when(userService.findById(testUser.getId())).thenReturn(testUser);
        when(taskRepository.save(taskEntity)).thenReturn(savedTaskEntity);

        TaskEntity result = taskService.save(taskEntity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Title Entity Task", result.getTitle());
        assertEquals("Description Entity Task", result.getDescription());
        assertEquals(LocalDate.of(2025, 12, 31), result.getDueDate());
        assertFalse(result.getCompleted());

        verify(userService).findById(testUser.getId());
        verify(taskRepository).save(taskEntity);
    }

    @Test
    public void testThatShouldFindAllTasksSuccessfully() {
        TaskEntity savedTaskEntityA = createTestTaskEntity();
        savedTaskEntityA.setId(1L);
        TaskEntity savedTaskEntityB = TaskTestDataUtil.createTestTaskEntity();
        savedTaskEntityB.setId(2L);

        List<TaskEntity> mockList = List.of(savedTaskEntityA, savedTaskEntityB);
        Page<TaskEntity> mockPage = new PageImpl<>(mockList);

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<TaskEntity> resultPage = taskService.findAll(PageRequest.of(0, 10));

        assertNotNull(resultPage);
        assertEquals(2, resultPage.getContent().size());

        assertEquals("Title Entity Task", resultPage.getContent().get(0).getTitle());
        assertEquals("Title Entity Task", resultPage.getContent().get(1).getTitle());

        verify(taskRepository).findAll(any(Pageable.class));
    }

    @Test
    public void testThatShouldFindTaskByIdSuccessfully() {
        TaskEntity savedTaskEntity = createTestTaskEntity();
        savedTaskEntity.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(savedTaskEntity));

        TaskEntity result = taskService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Title Entity Task", result.getTitle());
        assertEquals("Description Entity Task", result.getDescription());
        assertEquals(LocalDate.of(2025, 12, 31), result.getDueDate());
        assertFalse(result.getCompleted());

        verify(taskRepository).findById(1L);
    }

    @Test
    public void testThatShouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.findById(99L)
        );

        assertEquals("Task not found with id: 99", exception.getMessage());
        verify(taskRepository).findById(99L);
    }

    @Test
    public void testThatShouldPartiallyUpdateTaskSuccessfully() {
        TaskEntity existingTask = createTestTaskEntity();
        existingTask.setId(1L);

        TaskEntity partialUpdateData = createTestTaskEntity();
        partialUpdateData.setId(existingTask.getId());

        partialUpdateData.setTitle("Updated Title");

        when(taskRepository.findById(existingTask.getId())).thenReturn(Optional.of(existingTask));

        TaskEntity expectedUpdatedTask = TaskEntity.builder()
                .title(partialUpdateData.getTitle())
                .description(existingTask.getDescription())
                .dueDate(existingTask.getDueDate())
                .build();
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(expectedUpdatedTask);

        TaskEntity result = taskService.partialUpdate(existingTask.getId(), partialUpdateData);

        assertEquals(partialUpdateData.getTitle(), result.getTitle());
        assertEquals(existingTask.getDescription(), result.getDescription());
        assertEquals(existingTask.getDueDate(), result.getDueDate());

        verify(taskRepository).findById(existingTask.getId());
        verify(taskRepository).save(any(TaskEntity.class));
    }

    @Test
    public void testThatDeleteTaskByIdSuccessfully() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.deleteById(taskId);

        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    public void testThatDeleteByIdThrowsWhenNotFound() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(TaskNotFoundException.class,
                () -> taskService.deleteById(taskId));

        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).deleteById(anyLong());
    }
}
