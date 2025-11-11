package com.berkay.pollbackend.controller.auth;

import com.berkay.pollbackend.dto.jwt.JwtAuthenticationResponse;
import com.berkay.pollbackend.dto.login.LoginRequest;
import com.berkay.pollbackend.dto.signup.SignUpRequest;
import com.berkay.pollbackend.model.role.Role;
import com.berkay.pollbackend.model.role.RoleName;
import com.berkay.pollbackend.model.User;
import com.berkay.pollbackend.repository.RoleRepository;
import com.berkay.pollbackend.repository.UserRepository;
import com.berkay.pollbackend.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(value = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;


    @Test
    void registerUserReturn201Created() throws Exception {

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("Test User");
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword123");

        Role userRole = new Role(RoleName.ROLE_USER);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole));

        User savedUser = new User("Test User", "testuser", "test@example.com", "hashedPassword123");
        savedUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(
                        post("/api/auth/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User Registered successfully"))
                .andExpect(header().string("Location", containsString("/api/users/testuser")));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUserNameExistsReturn400BadRequest() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("Test User");
        signUpRequest.setUsername("existinguser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        mockMvc.perform(
                        post("/api/auth/signup")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Username already in taken!"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUserSuccessfulReturn200OkWithJwt() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("testuser");
        loginRequest.setPassword("password123");

        String fakeJwt = "fake.jwt.token.string";

        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(mockAuthentication);

        when(jwtTokenProvider.generateToken(mockAuthentication)).thenReturn(fakeJwt);

        mockMvc.perform(
                        post("/api/auth/signin")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(fakeJwt))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticateUserReturn401Unauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("testuser");
        loginRequest.setPassword("wrongpassword");

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenThrow(new BadCredentialsException("Hatalı şifre"));

        mockMvc.perform(
                        post("/api/auth/signin")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isUnauthorized());

        verify(jwtTokenProvider, never()).generateToken(any(Authentication.class));
    }
}