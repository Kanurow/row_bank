package com.rowland.engineering.rowbank.controller;

import com.rowland.engineering.rowbank.dto.ApiResponse;
import com.rowland.engineering.rowbank.dto.JwtAuthenticationResponse;
import com.rowland.engineering.rowbank.dto.LoginRequest;
import com.rowland.engineering.rowbank.dto.RegisterRequest;
import com.rowland.engineering.rowbank.exception.AppException;
import com.rowland.engineering.rowbank.exception.UserNotFoundException;
import com.rowland.engineering.rowbank.model.Role;
import com.rowland.engineering.rowbank.model.RoleName;
import com.rowland.engineering.rowbank.model.User;
import com.rowland.engineering.rowbank.repository.RoleRepository;
import com.rowland.engineering.rowbank.repository.UserRepository;
import com.rowland.engineering.rowbank.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication - Registration /Sign In")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    private static final SecureRandom random = new SecureRandom();
    private static final int MAX_FAILED_ATTEMPTS = 3;


    @Operation(
            summary = "Enables user log in - Users can sign in using username or email address"
    )
    @PostMapping("/sign_in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + loginRequest.getUsernameOrEmail()));

        if (user.isAccountLocked()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Account is locked due to multiple failed login attempts. Please contact support."));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            user.setFailedLoginAttempts(0);
            user.setLastLoginAttempt(null);
            userRepository.save(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

        } catch (BadCredentialsException e) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            user.setLastLoginAttempt(LocalDateTime.now());

            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.setAccountLocked(true);
            }

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid username or password. Attempts remaining: " + (MAX_FAILED_ATTEMPTS - user.getFailedLoginAttempts())));
        }
    }


    @Operation(
            summary = "Enables user registration - To sign up with admin role, add `row` to email field."
    )
    @PostMapping("/sign_up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        String accountNumber = generateAccountNumber();
        Optional<User> foundUser = userRepository.findByAccountNumber(accountNumber);
        while (foundUser.isEmpty()) {
            accountNumber = generateAccountNumber();
        }

        User user = new User(registerRequest.getBankName(), registerRequest.getFirstName(), registerRequest.getLastName(),
                registerRequest.getDateOfBirth(), registerRequest.getUsername(),
                registerRequest.getEmail(), registerRequest.getPassword(),
                registerRequest.getOpeningBalance()
        );
            
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAccountNumber(accountNumber);

        Role userRole;

        if (registerRequest.getEmail().contains("row")) {
            userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException("Admin Role not set."));
        } else {
            userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));
        }

        user.setRoles(Collections.singleton(userRole));
        user.setAccountLocked(false);

        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/users/{username}")
                .buildAndExpand(savedUser.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }



    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generateRawBankAccountNumber();
        } while (userRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private String generateRawBankAccountNumber() {
        int randomNumber = random.nextInt(1_000_000_000);
        return String.format("22%09d", randomNumber);
    }


}
