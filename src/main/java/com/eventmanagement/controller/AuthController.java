package com.eventmanagement.controller;

import com.eventmanagement.dto.LoginRequestDTO;
import com.eventmanagement.dto.AuthResponseDTO;
import com.eventmanagement.dto.UserRequestDTO;
import com.eventmanagement.dto.UserResponseDTO;
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

    @Autowired
    private UserService userService;

    // Note: Pour l'instant, on simule l'authentification
    // Dans la prochaine étape, nous implémenterons JWT

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
            // Pour l'instant, on retourne juste l'utilisateur
            // Dans la prochaine étape, nous ajouterons JWT
            UserResponseDTO user = userService.findByEmail(loginRequestDTO.getEmail());

            AuthResponseDTO authResponse = new AuthResponseDTO();
            authResponse.setUser(user);
            authResponse.setToken("dummy-token"); // Temporaire
            authResponse.setRoles(user.getRoles());

            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}