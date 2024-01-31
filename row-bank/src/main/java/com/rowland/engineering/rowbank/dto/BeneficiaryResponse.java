package com.rowland.engineering.rowbank.dto;

import com.rowland.engineering.rowbank.model.BankName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BeneficiaryResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String accountNumber;
    private String email;
    private BankName bankName;
}
