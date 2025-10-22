package com.berkay.pollbackend.controller.auth;

import com.berkay.pollbackend.dto.api.ApiResponse;
import com.berkay.pollbackend.dto.jwt.JwtAuthenticationResponse;
import com.berkay.pollbackend.dto.login.LoginRequest;
import com.berkay.pollbackend.dto.signup.SignUpRequest;
import com.berkay.pollbackend.exception.AppException;
import com.berkay.pollbackend.model.role.Role;
import com.berkay.pollbackend.model.role.RoleName;
import com.berkay.pollbackend.model.User;
import com.berkay.pollbackend.repository.RoleRepository;
import com.berkay.pollbackend.repository.UserRepository;
import com.berkay.pollbackend.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username already in taken!"),
                    HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail()
                , signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() ->
                new AppException("User Role not set"));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(locationUri)
                .body(new ApiResponse(true, "User Registered successfully"));
    }
}
