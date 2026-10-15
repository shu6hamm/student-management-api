package com.shubham.student_management.service;

import com.shubham.student_management.dto.AuthResponse;
import com.shubham.student_management.dto.LoginRequest;
import com.shubham.student_management.dto.RegisterRequest;
import com.shubham.student_management.entity.Role;
import com.shubham.student_management.entity.User;
import com.shubham.student_management.repository.UserRepository;
import com.shubham.student_management.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.shubham.student_management.exception.DuplicateResourceException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    public String register(RegisterRequest request) {
        // Plain exceptions for now; Step 11 turns them into clean error responses
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // hash, never plain
        user.setRole(Role.USER);                                         // role is set by server
        userRepository.save(user);

        return "User registered successfully";
    }

    public AuthResponse login(LoginRequest request) {
        // Throws an exception if username/password is wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}