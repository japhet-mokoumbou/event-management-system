package com.eventmanagement.service;

import com.eventmanagement.config.JwtTokenProvider;
import com.eventmanagement.dto.AuthResponseDTO;
import com.eventmanagement.dto.LoginRequestDTO;
import com.eventmanagement.dto.UserResponseDTO;
import com.eventmanagement.entity.User;
import com.eventmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);

            User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

            Set<String> roles = user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toSet());

            return new AuthResponseDTO(
                    token,
                    "Bearer",
                    user.getId(),
                    user.getFullname(),
                    user.getEmail(),
                    roles
            );
        }catch (BadCredentialsException e){
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }
    }

    public AuthResponseDTO refereshToken(String refreshToken){
        // Pour simplifier, on utilise le même token
        // Dans une vraie application, on a un refresh token séparé
        if(jwtTokenProvider.validateToken(refreshToken)){
            String email = jwtTokenProvider.getUsernameFromToken(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

            Set<String> roles = user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toSet());

            return new AuthResponseDTO(
                    refreshToken,
                    "Bearer",
                    user.getId(),
                    user.getFullname(),
                    user.getEmail(),
                    roles
            );
        }
        throw new IllegalArgumentException("Token invalide");
    }

    public void logout(String token) {
        // Dans une vraie application, On ajoutera le token à une blacklist
        // Pour simplifier, on ne fait rien
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw  new RuntimeException("Utilisateur non authentifié");
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new UserResponseDTO(
                user.getId(),
                user.getFullname(),
                user.getEmail(),
                roles,
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
