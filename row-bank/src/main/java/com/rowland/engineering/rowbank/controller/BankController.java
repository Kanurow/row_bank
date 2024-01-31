package com.rowland.engineering.rowbank.controller;

import com.rowland.engineering.rowbank.dto.*;
import com.rowland.engineering.rowbank.security.CurrentUser;
import com.rowland.engineering.rowbank.security.UserPrincipal;
import com.rowland.engineering.rowbank.service.BankService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/banking")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/get-beneficiary")
    private BeneficiaryResponse getBeneficiaryDetails(@RequestBody BeneficiaryRequest beneficiaryRequest) {
        return bankService.getBeneficiaryDetails(beneficiaryRequest);
    }

    @PatchMapping("/make-transfer")
    private TransferResponse makeTransfer(@CurrentUser UserPrincipal currentUser,
                                          @RequestBody MakeTransfer makeTransfer) {
        return bankService.makeTransfer(makeTransfer, currentUser);
    }
}
