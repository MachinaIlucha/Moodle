package com.illiapinchuk.moodle.model.dto;

import com.illiapinchuk.moodle.model.entity.RoleName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** Incoming DTO to grant role to a user. */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class GrantRoleDto {

  @NotNull
  Long userId;

  @NotEmpty
  Set<RoleName> roles;
}
