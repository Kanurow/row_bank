package com.rowland.engineering.rowbank.dto;

import com.rowland.engineering.rowbank.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String firstName;

    private String lastName;

    private String username;

    private String accountNumber;

    private Date dateOfBirth;

    private String email;
    private BigDecimal balance;

    private Set<Role> roles;
}
