package com.eventmanagement.controller;

import com.eventmanagement.dto.LoginRequestDTO;
import com.eventmanagement.dto.AuthResponseDTO;
import com.eventmanagement.dto.UserRequestDTO;
import com.eventmanagement.dto.UserResponseDTO;
import com.eventmanagement.service.AuthService;
import com.eventmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService = authService;
        this.userService = userService;
    }

    /**
     * Inscription d'un nouvel utilisateur
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        try {
            UserResponseDTO user = userService.createUser(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Connexion d'un utilisateur
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
           AuthResponseDTO authResponse = authService.login(loginRequestDTO);
           return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestParam String refreshToken){

        try{
            AuthResponseDTO authResponse = authService.refereshToken(refreshToken);
            return ResponseEntity.ok(authResponse);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String token) {
        try {
            authService.logout(token);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        try {
            UserResponseDTO user = authService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}