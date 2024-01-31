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
public class BeneficiaryRequest {
    private String accountNumberOrEmail;
    private BankName bankName;
}
