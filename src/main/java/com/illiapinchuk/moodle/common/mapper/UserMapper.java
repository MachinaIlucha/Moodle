package com.illiapinchuk.moodle.common.mapper;

import com.illiapinchuk.moodle.model.dto.UserCreationDto;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * This interface defines methods for mapping between the {@link User}, {@link UserDto}
 * and {@link UserCreationDto} classes.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  /**
   * Maps a {@link User} object to a {@link UserDto} object.
   *
   * @param user The {@link User} object to be mapped.
   * @return The resulting {@link UserDto} object.
   */
  UserDto userToUserDto(User user);

  /**
   * Maps a {@link UserDto} object to a {@link User} object.
   *
   * @param userDto The {@link UserDto} object to be mapped.
   * @return The resulting {@link User} object.
   */
  User userDtoToUser(UserDto userDto);

  /**
   * Maps a {@link UserCreationDto} object to a {@link User} object.
   *
   * @param userCreationDto The {@link UserCreationDto} object to be mapped.
   * @return The resulting {@link User} object.
   */
  User userCreationDtoToUser(UserCreationDto userCreationDto);

  /**
   * This method updates the User object with the data from the UserDto.
   *
   * @param user    The User object to be updated.
   * @param userDto The source of the updated data.
   */
  void updateUser(@MappingTarget User user, UserDto userDto);
}
