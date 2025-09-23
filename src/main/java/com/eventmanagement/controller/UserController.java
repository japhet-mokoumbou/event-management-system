package com.eventmanagement.controller;

import com.eventmanagement.dto.UserRequestDTO;
import com.eventmanagement.dto.UserResponseDTO;
import com.eventmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO){

        try{
            UserResponseDTO user = userService.createUser(userRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id){
        try{
            UserResponseDTO user = userService.findById(id);
            return ResponseEntity.ok(user);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable){
        try{
            Page<UserResponseDTO> users = userService.findAll(pageable);
            return ResponseEntity.ok(users);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDTO>> getActiveUsers(){
        try{
            List<UserResponseDTO> users = userService.findActiveUsers();
            return ResponseEntity.ok(users);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO){
        try{
            UserResponseDTO user = userService.updateUser(id,userRequestDTO);
            return ResponseEntity.ok(user);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id){
        try{
            userService.deactivateUser(id);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id){
        try{
            userService.activateUser(id);
            return ResponseEntity.ok().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserResponseDTO> addRoleToUser(@PathVariable Long id, @RequestParam String roleName){

        try{
            UserResponseDTO user = userService.addRoleToUser(id,roleName);
            return ResponseEntity.ok(user);
        }catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}/roles")
    public ResponseEntity<UserResponseDTO> removeRoleFromUser(@PathVariable Long id, @RequestParam String roleName){
        try{
            UserResponseDTO user = userService.removeRoleFromUser(id, roleName);
            return ResponseEntity.ok(user);
        }catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
