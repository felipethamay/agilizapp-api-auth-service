package com.example.agilizapp.api.auth.service.controller.auth;

import com.example.agilizapp.api.auth.service.repository.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  private String email;
  String password;
  private Role role;
}
