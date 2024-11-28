package com.example.agilizapp.api.auth.service.repository.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
  ADMIN(
          Set.of(
                  Permission.ADMIN_READ,
                  Permission.ADMIN_UPDATE,
                  Permission.ADMIN_DELETE,
                  Permission.ADMIN_CREATE,
                  Permission.MANAGER_READ,
                  Permission.MANAGER_UPDATE,
                  Permission.MANAGER_DELETE,
                  Permission.MANAGER_CREATE
          ), "Administrador"
  ),
  USER(Collections.emptySet(),
          "Usuário"),
  ANALYST(Collections.emptySet(),
          "Analista")
  ;

  @Getter
  private final Set<Permission> permissions;
  @Getter
  private final String description;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
