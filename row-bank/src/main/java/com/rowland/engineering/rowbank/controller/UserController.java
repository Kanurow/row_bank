package com.rowland.engineering.rowbank.controller;


import com.rowland.engineering.rowbank.dto.*;
import com.rowland.engineering.rowbank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(
            summary = "Make deposit into user personal account"
    )
    @PatchMapping("/make-deposit/{userId}")
    public ResponseEntity<ApiResponse> depositIntoUserAccount(@Valid @RequestBody MakeDeposit deposit, @PathVariable Long userId) {
        userService.makeDeposit(deposit, userId);
        return ResponseEntity.ok(new ApiResponse(true,
                "Account successfully credited with: #"+ deposit.getDepositAmount()));
    }

    @Operation(
            description = "Get user by Id",
            summary = "Returns user by providing user id"
    )
    @GetMapping("/{id}")
    public Optional<UserResponse> getUserById(@PathVariable(value = "id") Long userId) {
        return userService.findUserDetails(userId);
    }



}
