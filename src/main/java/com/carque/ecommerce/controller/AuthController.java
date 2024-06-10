package com.carque.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carque.ecommerce.model.User;
import com.carque.ecommerce.repository.UserRepository;
import com.carque.ecommerce.security.JwtUtils;
import com.carque.ecommerce.service.UserDetailsServiceImpl;

/**
 * AuthController
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final JwtUtils jwtUtils;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl,
            JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authRequest.getUsername());

        final String Jwt = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(Jwt);

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Username and Password cannot be empty");
        }

        User existingUser = userRepository.findByUsername(username);

        if (existingUser != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User newUser = new User(username, passwordEncoder.encode(password));
        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");

    }

    @Autowired
    public void setDependencies(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

}