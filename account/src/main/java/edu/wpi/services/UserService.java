package edu.wpi.services;

import edu.wpi.dto.UserDTO;
import edu.wpi.exceptions.ResourceNotFoundException;
import edu.wpi.repositories.UserRepository;
import edu.wpi.enties.ChangePasswordRequest;
import edu.wpi.enties.User;
import edu.wpi.request.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO getUserProfile(Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToDTO(user);
    }

    public UserDTO updateProfile(UpdateProfileRequest request, Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFirstname() != null) {
            user.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }

        user = userRepository.save(user);
        return mapToDTO(user);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        return dto;
    }
}