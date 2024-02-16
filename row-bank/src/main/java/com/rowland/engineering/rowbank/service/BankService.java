package com.rowland.engineering.rowbank.service;

import com.rowland.engineering.rowbank.dto.*;
import com.rowland.engineering.rowbank.exception.*;
import com.rowland.engineering.rowbank.model.*;
import com.rowland.engineering.rowbank.repository.SavingHistoryRepository;
import com.rowland.engineering.rowbank.repository.SavingRepository;
import com.rowland.engineering.rowbank.repository.TransactionRepository;
import com.rowland.engineering.rowbank.repository.UserRepository;
import com.rowland.engineering.rowbank.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankService {

    public static final double FLEXIBLE_INTEREST_RATE = 0.07;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final SavingRepository savingRepository;
    private final SavingHistoryRepository savingHistoryRepository;


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
        if (receiver.isEmpty() || sender.isEmpty()) {
            throw new UserNotFoundException("User not found! Check details");
        }

        if (makeTransfer.getBankToBeCredited() != receiver.get().getBankName()) {
            throw new IncorrectBankNameException("Check bank name details!");
        } else if (! ((makeTransfer.getAccountNumberOrEmail().equals(receiver.get().getEmail()) ||
                makeTransfer.getAccountNumberOrEmail().equals(receiver.get().getAccountNumber())) )
        ) {
            throw new AccountDetailsMismatch("Account detail: " + makeTransfer.getAccountNumberOrEmail() + " details do not match");
        } else if (Objects.equals(currentUser.getId(), receiver.get().getId())
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

    @Transactional
    public SavingResponse createFlexibleSavingPlan(SavingRequest savingRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + currentUser.getId()));
        if (savingRequest.getAmount() == null) {
            throw new IllegalArgumentException("Invalid input: All fields are required.");
        } else if (user.getBalance().subtract(savingRequest.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundException("You cannot save more than your current balance!.");
        } else if (savingRequest.getSavingType() != SavingType.FLEXIBLE) {
            throw new IncorrectSavingTypeException("Incorrect saving type found.");
        }

        LocalDateTime startDate = LocalDateTime.now();

        Saving saving = new Saving();
        saving.setUser(user);
        saving.setAmount(savingRequest.getAmount());
        saving.setDescription(savingRequest.getDescription());
        saving.setIsActive(true);
        saving.setSavingType(SavingType.FLEXIBLE);
        saving.setInterestEarned(BigDecimal.ZERO);
        saving.setInterestRate(BigDecimal.valueOf(FLEXIBLE_INTEREST_RATE));
        saving.setStartDate(LocalDate.from(startDate));

        Saving save = savingRepository.save(saving);

        user.setBalance(user.getBalance().subtract(savingRequest.getAmount()));
        user.getSavings().add(save);

        userRepository.save(user);
        return SavingResponse.builder()
                .savingType(SavingType.FLEXIBLE)
                .amount(savingRequest.getAmount())
                .startDate(startDate)
                .description(savingRequest.getDescription())
                .message("Saving successfully created!")
                .build();
    }

    @Transactional
    public SavingResponse topUpFlexibleSavingPlan(TopUpSavings savingRequest, UserPrincipal currentUser) {
        User saver = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (saver.getBalance().subtract(savingRequest.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundException("You cannot save more than your current balance!.");
        } else if (savingRequest.getSavingType() != SavingType.FLEXIBLE) {
            throw new IncorrectSavingTypeException("Incorrect saving type found.");
        }
        Saving userSaving = savingRepository.findByIdAndUser(savingRequest.getSavingId(), saver);

        BigDecimal newAmount = userSaving.getAmount().add(savingRequest.getAmount());
        userSaving.setAmount(newAmount);

        SavingHistory savingHistory = SavingHistory.builder()
                    .date(LocalDateTime.now())
                    .type(TransactionType.CREDIT)
                    .amount(savingRequest.getAmount())
                    .savings(userSaving)
                    .build();
        savingHistoryRepository.save(savingHistory);

        return SavingResponse.builder()
                    .amount(savingRequest.getAmount())
                    .description(savingRequest.getDescription())
                    .startDate(LocalDateTime.now())
                    .message("Top up completed!")
                    .build();

    }

    public SavingResponse withdrawFromFlexibleSaving(WithdrawFromSaving withdrawFromSaving, UserPrincipal currentUser) {
        User saver = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Saving userSaving = savingRepository.findByIdAndUser(withdrawFromSaving.getSavingId(), saver);
        if (userSaving == null)
            throw  new SavingNotFoundException("Saving does not exist for user");

        if (userSaving.getAmount().subtract(withdrawFromSaving.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundException("You cannot withdraw more than the amount in your savings");
        } else if (withdrawFromSaving.getSavingType() != SavingType.FLEXIBLE) {
            throw new IncorrectSavingTypeException("Incorrect saving type found.");
        }
        BigDecimal newSavingBalance = userSaving.getAmount().subtract(withdrawFromSaving.getAmount());
        userSaving.setAmount(newSavingBalance);
        BigDecimal newBalance = saver.getBalance().add(withdrawFromSaving.getAmount());
        saver.setBalance(newBalance);
        userRepository.save(saver);
        savingRepository.save(userSaving);
        SavingHistory savingHistory = SavingHistory.builder()
                .date(LocalDateTime.now())
                .type(TransactionType.DEBIT)
                .amount(withdrawFromSaving.getAmount())
                .savings(userSaving)
                .build();
        savingHistoryRepository.save(savingHistory);


        return SavingResponse.builder()
                .message("Withdrawal successful!")
                .amount(withdrawFromSaving.getAmount())
                .build();

    }
}


