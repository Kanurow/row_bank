package com.rowland.engineering.rowbank.service;

import com.rowland.engineering.rowbank.dto.*;
import com.rowland.engineering.rowbank.exception.AccountDetailsMismatch;
import com.rowland.engineering.rowbank.exception.IncorrectBankNameException;
import com.rowland.engineering.rowbank.exception.InsufficientFundException;
import com.rowland.engineering.rowbank.exception.UserNotFoundException;
import com.rowland.engineering.rowbank.model.Transaction;
import com.rowland.engineering.rowbank.model.TransactionType;
import com.rowland.engineering.rowbank.model.User;
import com.rowland.engineering.rowbank.repository.TransactionRepository;
import com.rowland.engineering.rowbank.repository.UserRepository;
import com.rowland.engineering.rowbank.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public BeneficiaryResponse getBeneficiaryDetails(BeneficiaryRequest beneficiaryRequest) {
        Optional<User> foundUserAccount = userRepository.findByAccountNumberOrEmail(beneficiaryRequest.getAccountNumberOrEmail(), beneficiaryRequest.getAccountNumberOrEmail());
        if (foundUserAccount.isEmpty()) {
            throw new UserNotFoundException(beneficiaryRequest.getAccountNumberOrEmail() + " not found!!! Check details");
        }
        if (foundUserAccount.get().getBankName() != beneficiaryRequest.getBankName()) {
            throw new IncorrectBankNameException(beneficiaryRequest.getAccountNumberOrEmail()
                    + " is not a customer at " + beneficiaryRequest.getBankName());
        }
        BeneficiaryResponse beneficiaryResponse = new BeneficiaryResponse();
        beneficiaryResponse.setId(foundUserAccount.get().getId());
        beneficiaryResponse.setEmail(foundUserAccount.get().getEmail());
        beneficiaryResponse.setFirstName(foundUserAccount.get().getFirstName());
        beneficiaryResponse.setLastName(foundUserAccount.get().getLastName());
        beneficiaryResponse.setAccountNumber(foundUserAccount.get().getAccountNumber());
        beneficiaryResponse.setUsername(foundUserAccount.get().getUsername());
        beneficiaryResponse.setBankName(foundUserAccount.get().getBankName());
        return beneficiaryResponse;
    }


    @Transactional
    public TransferResponse makeTransfer(MakeTransfer makeTransfer, UserPrincipal currentUser) {
        Optional<User> receiver = userRepository.findByAccountNumberOrEmail(makeTransfer.getAccountNumberOrEmail(),
                makeTransfer.getAccountNumberOrEmail());
        Optional<User> sender = userRepository.findById(currentUser.getId());
        if (receiver.isEmpty()) {
            throw new UserNotFoundException("User not found! Check details");
        }
        if (makeTransfer.getBankToBeCredited() != receiver.get().getBankName()) {
            throw new IncorrectBankNameException("Check bank name details!");
        } else if (! ((makeTransfer.getAccountNumberOrEmail().equals(receiver.get().getEmail()) ||
                makeTransfer.getAccountNumberOrEmail().equals(receiver.get().getAccountNumber())) )
        ) {
            throw new AccountDetailsMismatch("Account detail: " + makeTransfer.getAccountNumberOrEmail() + " details do not match");
        } else if (Objects.equals(receiver.get().getId(), sender.get().getId())
        ) {
            throw new AccountDetailsMismatch("You cannot transfer into your own account");
        }

        User receiverAccount = receiver.get();
        User senderAccount = sender.get();
        BigDecimal transferAmount = makeTransfer.getTransferAmount();

        if (senderAccount.getBalance().subtract(transferAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundException("Insufficient fund to cover transfer");
        }
        Transaction senderTransaction = Transaction.builder()
                .user(senderAccount)
                .description(makeTransfer.getDescription())
                .timestamp(LocalDateTime.now())
                .amount(makeTransfer.getTransferAmount())
                .bankName(senderAccount.getBankName())
                .transactionType(TransactionType.DEBIT)
                .build();
        Transaction receiverTransaction = Transaction.builder()
                .user(receiverAccount)
                .description(makeTransfer.getDescription())
                .timestamp(LocalDateTime.now())
                .amount(makeTransfer.getTransferAmount())
                .bankName(receiverAccount.getBankName())
                .transactionType(TransactionType.CREDIT)
                .build();

        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);

        senderAccount.setBalance(senderAccount.getBalance().subtract(transferAmount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transferAmount));
        User savedSender = userRepository.save(senderAccount);
        userRepository.save(receiverAccount);
        return TransferResponse.builder()
                .amount(makeTransfer.getTransferAmount())
                .remainingSenderBalance(savedSender.getBalance())
                .receiverFullName(receiverAccount.getFirstName() + " " + receiverAccount.getLastName())
                .build();

    }
}
