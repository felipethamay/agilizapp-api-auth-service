package com.example.agilizapp.api.auth.service.repository;

import com.example.agilizapp.api.auth.service.repository.entity.user.UserEntity;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

  Optional<UserEntity> findUserEntityByEmail(String email);
  Optional<UserEntity> findUserEntityByEmailAndStatus(String userId, UserStatusEnum status);
  List<UserEntity> findAllByStatus(UserStatusEnum status);

}
