package com.rowland.engineering.rowbank.controller;

import com.rowland.engineering.rowbank.dto.*;
import com.rowland.engineering.rowbank.security.CurrentUser;
import com.rowland.engineering.rowbank.security.UserPrincipal;
import com.rowland.engineering.rowbank.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/banking")
@RequiredArgsConstructor
@Tag(name = "Banking")
public class BankController {

    private final BankService bankService;

    @Operation(
            summary = "Used for getting and confirming user details before making a transfer"
    )
    @GetMapping("/get-beneficiary")
    private BeneficiaryResponse getBeneficiaryDetails(@Valid @RequestBody BeneficiaryRequest beneficiaryRequest) {
        return bankService.getBeneficiaryDetails(beneficiaryRequest);
    }

    @Operation(
            summary = "For funds transfer from logged in user to a different user"
    )
    @PatchMapping("/make-transfer")
    private TransferResponse makeTransfer(@Valid @CurrentUser UserPrincipal currentUser,
                                          @RequestBody MakeTransfer makeTransfer) {
        return bankService.makeTransfer(makeTransfer, currentUser);
    }

    @Operation(
            summary = "Gives users a more flexible saving habit"
    )
    @PostMapping("/create-flexible-saving")
    private SavingResponse createSavingPlan(@Valid @CurrentUser UserPrincipal currentUser,
                                                 @RequestBody SavingRequest savingRequest) {
        return bankService.createFlexibleSavingPlan(savingRequest, currentUser);
    }
    @Operation(
            summary = "Add more funds to existing flexible saving"
    )
    @PatchMapping("/top-up-flexible-saving")
    private SavingResponse topUpFlexibleSaving(@Valid @CurrentUser UserPrincipal currentUser,
                                            @RequestBody TopUpSavings topUpSavings) {
        return bankService.topUpFlexibleSavingPlan(topUpSavings, currentUser);
    }

    @Operation(
            summary = "Withdraw funds from existing flexible saving"
    )
    @PatchMapping("/withdraw-from-flexible-saving")
    private SavingResponse withdrawFromFlexibleSaving(@Valid @CurrentUser UserPrincipal currentUser,
                                               @RequestBody WithdrawFromSaving withdrawFromSaving) {
        return bankService.withdrawFromFlexibleSaving(withdrawFromSaving, currentUser);
    }

    @Operation(
            summary = "Deletes flexible saving plan and credits earned interest and base balance to user"
    )
    @DeleteMapping("/delete-flexible-saving/{id}")
    private ResponseEntity<String> deleteFlexibleSaving(@Valid @CurrentUser UserPrincipal currentUser,
                                                      @PathVariable(value = "id") Long id) {
        boolean isDeleted = bankService.deleteFlexibleSaving(id, currentUser);
        if (isDeleted)
            return new ResponseEntity<>("Successfully deleted", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>("Error! Not found", HttpStatus.NOT_FOUND);
    }

}
