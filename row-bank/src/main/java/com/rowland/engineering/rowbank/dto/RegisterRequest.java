package com.rowland.engineering.rowbank.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 2,max = 40, message = "First name should be between two characters and forty characters")
    private String firstName;
    @NotBlank
    @Size(min = 2,max = 40, message = "Last name should be between two characters and forty characters")
    private String lastName;

    private Date dateOfBirth;

    @NotBlank(message ="Username must not be blank")
    @Size(min = 2,max = 40 , message = "Last name should be between two characters and forty characters")
    private String username;

    @PositiveOrZero
    private BigDecimal openingBalance;


    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message ="Password must not be blank")
    @Size(min = 4)
    private String password;
}
