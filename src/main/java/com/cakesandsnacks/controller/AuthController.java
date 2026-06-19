package com.cakesandsnacks.controller;

import com.cakesandsnacks.dto.AuthResponseDTO;
import com.cakesandsnacks.dto.UserLoginDTO;
import com.cakesandsnacks.dto.UserRegistrationDTO;
import com.cakesandsnacks.dto.UserDTO;
import com.cakesandsnacks.entity.User;
import com.cakesandsnacks.service.UserService;
import com.cakesandsnacks.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody UserRegistrationDTO dto) {
        UserDTO userDTO = userService.registerUser(dto);
        User user = userService.findByEmail(dto.getEmail());

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        AuthResponseDTO response = new AuthResponseDTO(
                token,
                refreshToken,
                userDTO,
                user.getRole().toString()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userService.findByEmail(dto.getEmail());

        // Extract dateOfBirth from user profile if available
        LocalDate dob = (user.getUserProfile() != null) ? user.getUserProfile().getDateOfBirth() : null;

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                dob,  // dateOfBirth
                user.getPhone(),
                user.getRole().toString(),
                user.getProfileImage(),
                user.getIsActive(),
                user.getCreatedAt()
        );

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        AuthResponseDTO response = new AuthResponseDTO(
                token,
                refreshToken,
                userDTO,
                user.getRole().toString()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestParam String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String email = jwtTokenProvider.getEmailFromToken(refreshToken);
            User user = userService.findByEmail(email);

            LocalDate dob = (user.getUserProfile() != null) ? user.getUserProfile().getDateOfBirth() : null;

            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    dob,
                    user.getPhone(),
                    user.getRole().toString(),
                    user.getProfileImage(),
                    user.getIsActive(),
                    user.getCreatedAt()
            );

            String newToken = jwtTokenProvider.generateToken(email, user.getRole().toString());
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

            AuthResponseDTO response = new AuthResponseDTO(
                    newToken,
                    newRefreshToken,
                    userDTO,
                    user.getRole().toString()
            );

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}