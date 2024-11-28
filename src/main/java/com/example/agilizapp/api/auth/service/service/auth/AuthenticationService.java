package com.example.agilizapp.api.auth.service.service.auth;

import com.example.agilizapp.api.auth.service.config.JwtService;
import com.example.agilizapp.api.auth.service.controller.auth.AuthenticationRequest;
import com.example.agilizapp.api.auth.service.controller.auth.AuthenticationResponse;
import com.example.agilizapp.api.auth.service.exception.ResourceNotFoundException;
import com.example.agilizapp.api.auth.service.exception.UnauthorizedException;
import com.example.agilizapp.api.auth.service.model.User;
import com.example.agilizapp.api.auth.service.repository.TokenRepository;
import com.example.agilizapp.api.auth.service.repository.UserRepository;
import com.example.agilizapp.api.auth.service.repository.entity.token.Token;
import com.example.agilizapp.api.auth.service.repository.entity.token.TokenType;
import com.example.agilizapp.api.auth.service.repository.entity.user.Role;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserEntity;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserStatusEnum;
import com.example.agilizapp.api.auth.service.util.UserConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.example.agilizapp.api.auth.service.constant.Messages.USER_EMAIL_PASSWORD_ERROR;
import static com.example.agilizapp.api.auth.service.constant.Messages.USER_NOT_FOUND_USER_EMAIL;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public AuthenticationResponse getAccessToken(AuthenticationRequest request) {
        log.info("Obtendo access token para o usuário {}", request.getEmail());
        var email = request.getEmail();
        var user = userRepository.findUserEntityByEmailAndStatus(request.getEmail(), UserStatusEnum.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_USER_EMAIL + email));
        var iguais = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!iguais) {
            throw new UnauthorizedException(USER_EMAIL_PASSWORD_ERROR + user.getEmail());
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserSessionInRedis(user.getEmail(), jwtToken, refreshToken);
        saveUserToken(user, jwtToken);

        String role = String.valueOf(user.getRole());
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.valueOf(role))
                .build();
    }

    private void saveUserSessionInRedis(String email, String accessToken, String refreshToken) {
        redisTemplate.opsForHash().put("session:" + email, "accessToken", accessToken);
        redisTemplate.opsForHash().put("session:" + email, "refreshToken", refreshToken);

        redisTemplate.expire("session:" + email, 1, TimeUnit.HOURS);
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findUserEntityByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(UserConverter.userEntityToUser(user));
                saveUserToken(user, accessToken);

                saveUserSessionInRedis(user.getEmail(), accessToken, refreshToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAllUserTokens(User user) {
        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var validUserTokens = tokenRepository.findAllValidTokenByUser(userEntity);
        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

}
