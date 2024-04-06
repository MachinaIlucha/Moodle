package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.GrantRoleDto;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import com.illiapinchuk.moodle.service.business.AdminService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Service for admin operations. */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;

  @Override
  public void grantRole(@Nonnull final GrantRoleDto grantRoleDto) {
    final var user =
        userRepository
            .findById(grantRoleDto.getUserId())
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        "User not found with id: " + grantRoleDto.getUserId()));

    user.getRoles().addAll(grantRoleDto.getRoles()); // Add new roles to the existing ones
    userRepository.save(user); // Save the updated user
  }

  @Override
  public void removeRole(@Nonnull final GrantRoleDto grantRoleDto) {
    final var user =
        userRepository
            .findById(grantRoleDto.getUserId())
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        "User not found with id: " + grantRoleDto.getUserId()));

    user.getRoles().removeAll(grantRoleDto.getRoles());
    userRepository.save(user);
  }
}
