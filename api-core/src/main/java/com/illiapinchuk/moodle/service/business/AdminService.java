package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.model.dto.GrantRoleDto;

/** Service for admin operations. */
public interface AdminService {

  /**
   * Grant a role to a user.
   *
   * @param grantRoleDto the DTO to grant a role to a user
   */
  void grantRole(GrantRoleDto grantRoleDto);
}
