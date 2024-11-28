package com.example.agilizapp.api.auth.service.repository.entity.token;

import com.example.agilizapp.api.auth.service.repository.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Token")
@Table(name = "TB_TOKEN")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Builder.Default
  private String tokenId = UUID.randomUUID().toString();

  @Column(unique = true)
  private String token;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType = TokenType.BEARER;

  private boolean revoked;

  private boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;
}
