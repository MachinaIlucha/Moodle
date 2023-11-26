package com.illiapinchuk.moodle.persistence.entity;

import com.illiapinchuk.moodle.model.entity.UserTokenStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents a user token entity in the database. This entity is used to store tokens associated
 * with user accounts.
 */
@Entity
@Table(name = "user_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserToken {

  @Id
  @Column(name = "user_id")
  Long userId;

  @Column(name = "token", nullable = false)
  String token;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  UserTokenStatus userTokenStatus;
}
