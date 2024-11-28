package com.example.agilizapp.api.auth.service.controller.auth;

import com.example.agilizapp.api.auth.service.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Authentication Controller", description = "API de autenticação")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/access-token")
    @Operation(summary = "Obter access token", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("Obtendo access token para o usuário {}", request.getEmail());
        return ResponseEntity.ok(authenticationService.getAccessToken(request));
    }
}
