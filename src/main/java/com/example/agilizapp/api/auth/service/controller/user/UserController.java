package com.example.agilizapp.api.auth.service.controller.user;

import com.example.agilizapp.api.auth.service.controller.auth.RegisterRequest;
import com.example.agilizapp.api.auth.service.model.User;
import com.example.agilizapp.api.auth.service.repository.entity.user.UserStatusEnum;
import com.example.agilizapp.api.auth.service.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User Controller", description = "API de usuário")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Buscar todos os usuários de acordo com o status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<List<User>> findUsersByStatus(@RequestParam UserStatusEnum status){
        log.info("Pesquisando todos os usuários com status {}", status.getDescription());
        return ResponseEntity.ok(userService.findUsersByStatus(status));
    }

    @PostMapping
    @Operation(summary = "Registrar novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<User> insertUser(@RequestBody RegisterRequest request) {
        log.info("Registrando novo usuário com email {}", request.getEmail());
        try {
            User user = userService.insertUser(request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Erro ao registrar usuário: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{userId}/authorize")
    @Operation(summary = "Autorizar usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<User> authorizeUser(@PathVariable String userId) {
        log.info("Autorizando usuário com userId {}", userId);
        return ResponseEntity.ok(userService.authorizeUser(userId));
    }

    @PostMapping("/{userId}/unauthorize")
    @Operation(summary = "Desautorizar usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<User> unauthorizeUser(@PathVariable String userId) {
        log.info("Desautorizando usuário com userId {}", userId);
        return ResponseEntity.ok(userService.unauthorizeUser(userId));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Deletar usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found", content ={
                    @Content(mediaType ="application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        log.info("Excluindo usuário com userId {}", userId);
        userService.deleteUserByUserId(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
    }

}