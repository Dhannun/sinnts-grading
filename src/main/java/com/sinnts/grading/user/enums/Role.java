package com.sinnts.grading.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.sinnts.grading.user.enums.Permission.*;

@RequiredArgsConstructor
@Getter
public enum Role {

  SUPER_ADMIN(
      Set.of(
          SUPER_ADMIN_CREATE,
          SUPER_ADMIN_READ,
          SUPER_ADMIN_UPDATE,
          SUPER_ADMIN_DELETE,

          ADMIN_CREATE,
          ADMIN_READ,
          ADMIN_UPDATE,
          ADMIN_DELETE,

          USER_CREATE,
          USER_READ,
          USER_UPDATE,
          USER_DELETE
      )
  ),
  ADMIN(
      Set.of(
          ADMIN_CREATE,
          ADMIN_READ,
          ADMIN_UPDATE,
          ADMIN_DELETE,

          USER_CREATE,
          USER_READ,
          USER_UPDATE,
          USER_DELETE
      )
  ),
  USER(
      Set.of(
          USER_CREATE,
          USER_READ,
          USER_UPDATE,
          USER_DELETE
      )
  );

  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>(getPermissions()
        .stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
