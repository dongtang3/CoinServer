package edu.wpi.controllers;

import edu.wpi.dto.UserDTO;
import edu.wpi.enties.ChangePasswordRequest;
import edu.wpi.request.UpdateProfileRequest;
import edu.wpi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(Principal connectedUser) {
        UserDTO profile = service.getUserProfile(connectedUser);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(
        @RequestBody UpdateProfileRequest request,
        Principal connectedUser
    ) {
        UserDTO updatedProfile = service.updateProfile(request, connectedUser);
        return ResponseEntity.ok(updatedProfile);
    }

}
