package com.benjamerc.taskmanager.service;

import com.benjamerc.taskmanager.directory.UserRepository;
import com.benjamerc.taskmanager.domain.entitiy.UserEntity;
import com.benjamerc.taskmanager.exception.user.UserIdMismatchException;
import com.benjamerc.taskmanager.exception.user.UserNotFoundException;
import com.benjamerc.taskmanager.service.impl.UserServiceImpl;
import com.benjamerc.taskmanager.util.UserTestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testThatCreateUserSuccessfully() {
        UserEntity user = UserTestDataUtil.createTestUserEntity();

        UserEntity savedUser = UserTestDataUtil.createTestUserEntity();
        savedUser.setId(1L);

        when(userRepository.save(user)).thenReturn(savedUser);

        UserEntity result = userService.save(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());

        verify(userRepository).save(user);
    }

    @Test
    public void testThatReturnsListOfAllUsersSuccessfully() {
        UserEntity savedUser1 = UserTestDataUtil.createTestUserEntity();
        savedUser1.setId(1L);
        savedUser1.setUsername("User 1");

        UserEntity savedUser2 = UserTestDataUtil.createTestUserEntity();
        savedUser2.setId(2L);
        savedUser2.setUsername("User 2");

        Iterable<UserEntity> userEntities = List.of(savedUser1, savedUser2);

        when(userRepository.findAll()).thenReturn(userEntities);

        List<UserEntity> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("User 1", result.get(0).getUsername());

        assertEquals(2L, result.get(1).getId());
        assertEquals("User 2", result.get(1).getUsername());

        verify(userRepository).findAll();
    }

    @Test
    public void testThatReturnsUserByIdSuccessfully() {
        Long userId = 1L;

        UserEntity savedUser = UserTestDataUtil.createTestUserEntity();
        savedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(savedUser));

        UserEntity result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getEmail(), result.getEmail());

        verify(userRepository).findById(userId);
    }

    @Test
    public void testThatThrowsUserNotFoundExceptionWhenReturnsUserById() {
        Long wrongId =  99L;

        when(userRepository.findById(wrongId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findById(wrongId)
        );

        assertEquals("User not found with id: " + wrongId, exception.getMessage());

        verify(userRepository).findById(wrongId);
    }

    @Test
    public void testThatPartialUpdatesUserSuccessfully() {
        Long userId = 1L;

        UserEntity savedUser = UserTestDataUtil.createTestUserEntity();
        savedUser.setId(userId);

        UserEntity update = UserTestDataUtil.createTestUserEntity();
        update.setId(userId);
        update.setUsername("Updated");
        update.setEmail(null);
        update.setPassword(null);

        UserEntity updatedUser = UserTestDataUtil.createTestUserEntity();
        updatedUser.setId(userId);
        updatedUser.setUsername(update.getUsername());

        when(userRepository.findById(userId)).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        UserEntity result = userService.partialUpdate(userId, update);

        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        assertEquals(updatedUser.getUsername(), result.getUsername());
        assertEquals(updatedUser.getEmail(), result.getEmail());

        verify(userRepository).findById(userId);
        verify(userRepository).save(updatedUser);
    }

    @Test
    public void testThatThrowsUserNotFoundExceptionWhenPartialUpdateGoesWrong() {
        Long wrongId = 99L;

        UserEntity update = UserTestDataUtil.createTestUserEntity();
        update.setId(wrongId);

        when(userRepository.findById(wrongId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.partialUpdate(wrongId, update)
        );

        assertEquals("User not found with id: " + wrongId, exception.getMessage());

        verify(userRepository).findById(wrongId);
    }

    @Test
    public void testThatThrowsIdMismatchExceptionWhenPartialUpdateGoesWrong() {
        Long pathId = 1L;
        Long bodyId = 2L;

        UserEntity update = UserTestDataUtil.createTestUserEntity();
        update.setId(bodyId);

        UserIdMismatchException exception = assertThrows(
                UserIdMismatchException.class,
                () -> userService.partialUpdate(pathId, update)
        );

        assertEquals("ID in path and body do not match", exception.getMessage());
    }

    @Test
    public void testThatDeletesUserByIdSuccessfully() {
        Long userId = 1L;

        UserEntity savedUser = UserTestDataUtil.createTestUserEntity();
        savedUser.setId(userId);

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> userService.deleteById(userId));

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testThatThrowsUserNotFoundExceptionWhenDeleteByIdGoesWrong() {
        Long wrongId = 99L;

        when(userRepository.existsById(wrongId)).thenReturn(false);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteById(wrongId)
        );

        assertEquals("User not found with id: " + wrongId, exception.getMessage());

        verify(userRepository).existsById(wrongId);
    }
}
