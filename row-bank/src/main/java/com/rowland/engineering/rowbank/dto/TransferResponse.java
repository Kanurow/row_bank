package com.rowland.engineering.rowbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class TransferResponse {
    private BigDecimal amount;
    private String receiverFullName;
    private BigDecimal remainingSenderBalance;
}
