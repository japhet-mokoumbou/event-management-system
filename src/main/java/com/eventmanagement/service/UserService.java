package com.eventmanagement.service;

import com.eventmanagement.dto.UserRequestDTO;
import com.eventmanagement.dto.UserResponseDTO;
import com.eventmanagement.entity.Role;
import com.eventmanagement.entity.User;
import com.eventmanagement.mapper.UserMapper;
import com.eventmanagement.repository.RoleRepository;
import com.eventmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserMapper userMapper){
        this.passwordEncoder=passwordEncoder;
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.userMapper=userMapper;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){

        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        User user = userMapper.toEntity(userRequestDTO);
        user.setPasswordHash(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setEnabled(true);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(()->new RuntimeException("Rôle USER introuvable"));
        user.getRoles().add(userRole);

        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO findById(Long id){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable avec l'ID: " + id));
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public Page<UserResponseDTO> findAll(Pageable pageable){

        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toResponseDTO);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID: " + id));

        if(!user.getEmail().equals(userRequestDTO.getEmail()) && userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw  new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        userMapper.updateEntity(userRequestDTO, user);

        if(userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()){
            user.setPasswordHash(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public void deactivateUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable: "+id));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public UserResponseDTO addRoleToUser(Long userId, String roleName){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvale: "+userId));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(()-> new RuntimeException("Rôle introuvale: "+roleName));
        user.getRoles().add(role);
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO removeRoleFromUser(Long userId, String roleName){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvale: "+userId));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(()-> new RuntimeException("Rôle introuvale: "+roleName));
        user.getRoles().remove(role);
        return userMapper.toResponseDTO(userRepository.save(user));
    }

}
