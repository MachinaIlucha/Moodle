package com.illiapinchuk.moodle.configuration.security.jwt;

import com.illiapinchuk.moodle.persistence.entity.User;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** Spring Security wrapper for class {@link com.illiapinchuk.moodle.persistence.entity.User}. */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class JwtUser implements UserDetails {

  @Serial static final long serialVersionUID = 8768341250592611450L;

  final Long id;
  final String login;
  final String email;
  final String password;

  final Collection<? extends GrantedAuthority> authorities;

  public static JwtUser build(@NotNull final User user) {
    final var authorities =
        user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.name()))
            .collect(Collectors.toList());

    return new JwtUser(
        user.getId(), user.getLogin(), user.getEmail(), user.getPassword(), authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return login;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
