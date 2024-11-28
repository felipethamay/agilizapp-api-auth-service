package com.example.agilizapp.api.auth.service.controller.auth;

import com.example.agilizapp.api.auth.service.repository.entity.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
  private String firstName;
  private String lastName;
  private Role role;
}
