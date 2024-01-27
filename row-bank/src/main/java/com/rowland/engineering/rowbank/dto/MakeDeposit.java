package com.rowland.engineering.rowbank.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeDeposit {

    @PositiveOrZero
    private BigDecimal depositAmount;
}
