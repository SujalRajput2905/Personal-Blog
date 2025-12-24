package com.example.personal_blog.controller;

import com.example.personal_blog.dto.LoginRequest;
import com.example.personal_blog.dto.LoginResponse;
import com.example.personal_blog.model.User;
import com.example.personal_blog.repository.UserRepository;
import com.example.personal_blog.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email already exists",HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole("USER");

        userRepository.save(user);

        return new ResponseEntity<>("User successfully registered", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            User user = userRepository
                    .findByUsername(loginRequest.getUsername())
                    .orElseThrow();

            String token = jwtUtil.generateToken(user.getUsername());

            return ResponseEntity.ok(
                    new LoginResponse(token, user.getRole())
            );

        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid Username or Password",
                    HttpStatus.UNAUTHORIZED);
        }
    }
}
