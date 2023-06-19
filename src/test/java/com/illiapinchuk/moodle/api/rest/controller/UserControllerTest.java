package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.common.mapper.UserMapper;
import com.illiapinchuk.moodle.exception.NotValidInputException;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

  private final static String LOGIN = "User Login";
  private final static String EMAIL = "User email";

  @Mock
  private UserService userService;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetUserByLoginOrEmail_WithLogin_ShouldReturnUserDto() {
    final var user = new User();
    final var userDto = new UserDto();
    final var expectedResponse = ResponseEntity.ok(userDto);

    when(userService.getUserByLoginOrEmail(LOGIN, null)).thenReturn(user);
    when(userMapper.userToUserDto(user)).thenReturn(userDto);

    final var actualResponse = userController.getUserByLoginOrEmail(LOGIN, null);

    assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
    assertEquals(expectedResponse.getBody(), actualResponse.getBody());
    verify(userService).getUserByLoginOrEmail(LOGIN, null);
    verify(userMapper).userToUserDto(user);
  }

  @Test
  void testGetUserByLoginOrEmail_WithInvalidInput_ShouldThrowNotValidInputException() {
    assertThrows(NotValidInputException.class,
        () -> userController.getUserByLoginOrEmail(LOGIN, EMAIL));
  }

  @Test
  void testCreateUser_ShouldReturnCreatedUserDto() {
    final var user = new User();
    final var userDto = new UserDto();
    final var expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(userDto);

    when(userMapper.userDtoToUser(userDto)).thenReturn(user);
    when(userService.createUser(user)).thenReturn(user);
    when(userMapper.userToUserDto(user)).thenReturn(userDto);

    final var actualResponse = userController.createUser(userDto);

    assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
    assertEquals(expectedResponse.getBody(), actualResponse.getBody());
    verify(userService).createUser(user);
    verify(userMapper).userToUserDto(user);
  }

  @Test
  void testUpdateUser_ShouldReturnUpdatedUserDto() {
    final var userDto = new UserDto();
    final var user = new User();
    final var expectedResponse = ResponseEntity.ok().body(userDto);

    when(userService.updateUser(userDto)).thenReturn(user);
    when(userMapper.userToUserDto(user)).thenReturn(userDto);

    final var actualResponse = userController.updateUser(userDto);

    assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
    assertEquals(expectedResponse.getBody(), actualResponse.getBody());
    verify(userService).updateUser(userDto);
    verify(userMapper).userToUserDto(user);
  }

  @Test
  void testDeleteUserByLoginOrEmail_ShouldReturnOkResponse() {
    final var response = userController.deleteUserByLoginOrEmail(LOGIN, null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(userService).deleteUserByLoginOrEmail(LOGIN, null);
  }
}
