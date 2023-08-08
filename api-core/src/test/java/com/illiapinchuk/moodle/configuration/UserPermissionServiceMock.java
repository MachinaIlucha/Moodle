package com.illiapinchuk.moodle.configuration;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

public class UserPermissionServiceMock {
  private static MockedStatic<UserPermissionService> mockedUserPermissionService;

  public static void start() {
    if (mockedUserPermissionService != null) {
      mockedUserPermissionService.close();
    }
    mockedUserPermissionService = mockStatic(UserPermissionService.class);
    mockedUserPermissionService
        .when(UserPermissionService::getJwtUser)
        .thenReturn(TestConstants.UserConstants.ADMIN_JWT_USER);
    mockedUserPermissionService.when(UserPermissionService::hasAnyRulingRole).thenReturn(true);
  }

  public static void stop() {
    if (mockedUserPermissionService != null) {
      mockedUserPermissionService.close();
      mockedUserPermissionService = null;
    }
  }
}
