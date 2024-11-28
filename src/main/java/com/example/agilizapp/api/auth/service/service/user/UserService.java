package com.example.agilizapp.api.auth.service.service.user;

import com.example.agilizapp.api.auth.service.controller.auth.RegisterRequest;
import com.example.agilizapp.api.auth.service.exception.BusinessException;
import com.example.agilizapp.api.auth.service.exception.ResourceNotFoundException;
import com.example.agilizapp.api.auth.service.model.User;
import com.example.agilizapp.api.auth.service.repository.UserRepository;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserEntity;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserStatusEnum;
import com.example.agilizapp.api.auth.service.util.UserConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.agilizapp.api.auth.service.constant.Messages.*;
import static com.example.agilizapp.api.auth.service.repository.entity.user.UserStatusEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findUsersByStatus(UserStatusEnum status){
        log.info("Pesquisando todos os usuários com status {}", status.getDescription());
        List<UserEntity> userEntityList = userRepository.findAllByStatus(status);
        return userEntityList.stream()
                .map(userEntity -> UserConverter.userEntityToUser(userEntity))
                .collect(Collectors.toList());
    }

    @Transactional
    public User insertUser(RegisterRequest request){
        log.info("Registrando novo usuário com email {}", request.getEmail());
        userRepository.findUserEntityByEmail(request.getEmail())
                .ifPresent(entity ->  new BusinessException(USER_EMAIL_ALREADY_EXISTS + request.getEmail()));
        var userEntity = UserConverter.registerRequestToUserEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        return UserConverter.userEntityToUser(userRepository.save(userEntity));
    }

    @Transactional
    public User authorizeUser(String userId){
        log.info("Autorizando usuário com userId {}", userId);
        var userEntity = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_USER_ID + userId));
        if (!PENDING_AUTHORIZATION.equals(userEntity.getStatus())){
            throw new BusinessException(USER_HAS_ALREADY_AUTHORIZED);
        }
        userEntity.setStatus(ACTIVE);
        return UserConverter.userEntityToUser(userRepository.save(userEntity));
    }

    @Transactional
    public User unauthorizeUser(String userId){
        log.info("Desautorizando usuário com userId {}", userId);
        var userEntity = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_USER_ID + userId));
        if (INACTIVE.equals(userEntity.getStatus())){
            throw new BusinessException(USER_HAS_ALREADY_UNAUTHORIZED);
        }
        userEntity.setStatus(INACTIVE);
        return UserConverter.userEntityToUser(userRepository.save(userEntity));
    }

    public User findUserById(String userId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_USER_ID + userId));
        return UserConverter.userEntityToUser(userEntity);
    }

    public User findUserByEmail(String userEmail){
        UserEntity userEntity =  userRepository.findUserEntityByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_USER_EMAIL + userEmail));
        return UserConverter.userEntityToUser(userEntity);
    }

    public UserEntity updateUser(String userId, UserEntity userEntity){
        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_USER_ID + userId));
        if (!userId.equals(userEntity.getUserId())){
            throw new BusinessException(USER_ID_URL_BODY);
        }
        return userRepository.save(userEntity);
    }

    public void deleteUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_USER_ID + userId));
        userRepository.delete(userEntity);
    }

}