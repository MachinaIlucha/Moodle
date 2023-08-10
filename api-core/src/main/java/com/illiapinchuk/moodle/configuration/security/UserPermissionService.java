package com.illiapinchuk.moodle.configuration.security;

import com.illiapinchuk.moodle.configuration.security.jwt.JwtUser;
import com.illiapinchuk.moodle.model.entity.RoleName;
import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A utility class for checking user permissions and fetching user-related data. This class provides
 * methods to obtain the current authenticated JWT user, check if the user has certain roles, and
 * determine if a user ID matches the authenticated user's ID.
 *
 * <p>Note: This class should not be instantiated, as it's a utility class.
 */
@UtilityClass
public class UserPermissionService {

  /**
   * Retrieves the current authenticated JWT user from the security context.
   *
   * @return the authenticated {@link JwtUser}.
   */
  public JwtUser getJwtUser() {
    return (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  /**
   * Checks if the current authenticated user has the 'ADMIN' role.
   *
   * @return {@code true} if the user has the 'ADMIN' role, {@code false} otherwise.
   */
  public boolean hasAdminRole() {
    return getJwtUser().getAuthorities().stream()
        .anyMatch(
            grantedAuthority -> grantedAuthority.getAuthority().equals(RoleName.ADMIN.name()));
  }

  /**
   * Checks if the current authenticated user has the 'USER' role.
   *
   * @return {@code true} if the user has the 'USER' role, {@code false} otherwise.
   */
  public boolean hasUserRole() {
    return getJwtUser().getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(RoleName.USER.name()));
  }

  /**
   * Determines if the authenticated user has any of the ruling roles (ADMIN, TEACHER, or
   * DEVELOPER).
   *
   * <p>The method fetches the current authenticated user's authorities and checks if any of these
   * authorities correspond to the ruling roles. A user is considered to have a ruling role if they
   * possess at least one of the specified roles.
   *
   * @return {@code true} if the user has any of the ruling roles (ADMIN, TEACHER, or DEVELOPER);
   *     {@code false} otherwise.
   */
  public boolean hasAnyRulingRole() {
    return getJwtUser().getAuthorities().stream()
        .anyMatch(
            grantedAuthority ->
                grantedAuthority.getAuthority().equals(RoleName.ADMIN.name())
                    || grantedAuthority.getAuthority().equals(RoleName.TEACHER.name())
                    || grantedAuthority.getAuthority().equals(RoleName.DEVELOPER.name()));
  }

  /**
   * Checks if the provided user ID matches the ID of the current authenticated user.
   *
   * @param userId the ID of the user to check against the authenticated user's ID.
   * @return {@code true} if the provided user ID matches the authenticated user's ID, {@code false}
   *     otherwise.
   */
  public boolean isSameUserAsAuthenticated(@Nonnull final Long userId) {
    return getJwtUser().getId().equals(userId);
  }
}
