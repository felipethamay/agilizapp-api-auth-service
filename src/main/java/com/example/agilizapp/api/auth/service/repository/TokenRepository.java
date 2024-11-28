package com.example.agilizapp.api.auth.service.repository;

import com.example.agilizapp.api.auth.service.repository.entity.token.Token;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
  List<Token> findAllValidTokenByUser(UserEntity user);

  Optional<Token> findByToken(String token);
}
