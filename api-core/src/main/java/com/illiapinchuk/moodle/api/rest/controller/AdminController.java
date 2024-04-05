package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.model.dto.GrantRoleDto;
import com.illiapinchuk.moodle.service.business.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for admins. */
@RestController
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

  private final AdminService adminService;

  /**
   * Grant a role to a user.
   *
   * @param grantRoleDto the DTO to grant a role to a user
   * @return a {@link ResponseEntity} with a suitable HTTP status code
   */
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER')")
  @PostMapping
  public ResponseEntity<Void> grantRole(@RequestBody @Valid final GrantRoleDto grantRoleDto) {
    adminService.grantRole(grantRoleDto);

    log.info("Role granted to user with id: {}", grantRoleDto.getUserId());
    return ResponseEntity.ok().build();
  }
}
