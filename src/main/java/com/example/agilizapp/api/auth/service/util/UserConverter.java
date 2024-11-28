package com.example.agilizapp.api.auth.service.util;

import com.example.agilizapp.api.auth.service.controller.auth.RegisterRequest;
import com.example.agilizapp.api.auth.service.model.User;
import com.example.agilizapp.api.auth.service.repository.entity.user.Role;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserEntity;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserStatusEnum;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UserConverter {

    public User userEntityToUser(final UserEntity userEntity) {
        return User.builder()
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .role(userEntity.getRole().getDescription())
                .status(userEntity.getStatus().getDescription())
                .build();
    }

    public UserEntity userToUserEntity(final User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(Role.valueOf(user.getRole()))
                .status(UserStatusEnum.valueOf(user.getStatus()))
                .build();
    }

    public UserEntity registerRequestToUserEntity(RegisterRequest request) {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .status(UserStatusEnum.PENDING_AUTHORIZATION)
                .role(request.getRole())
                .build();
    }

}