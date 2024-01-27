package com.rowland.engineering.rowbank.service;

import com.rowland.engineering.rowbank.dto.MakeDeposit;
import com.rowland.engineering.rowbank.dto.UserResponse;
import com.rowland.engineering.rowbank.dto.UserSummary;
import com.rowland.engineering.rowbank.exception.UserNotFoundException;
import com.rowland.engineering.rowbank.model.Transaction;
import com.rowland.engineering.rowbank.model.TransactionType;
import com.rowland.engineering.rowbank.model.User;
import com.rowland.engineering.rowbank.repository.TransactionRepository;
import com.rowland.engineering.rowbank.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void makeDeposit(MakeDeposit deposit, Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            user.setBalance(user.getBalance().add(deposit.getDepositAmount()));
            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.CREDIT)
                    .user(user)
                    .build();
            transactionRepository.save(transaction);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User with id: " +userId+ " not found");
        }
    }

    public Optional<UserResponse> findUserDetails(Long userId) {
        Optional<User> foundUser = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: "+ userId + "not found")));

        return foundUser.stream().map(user -> {
            UserResponse userDetail = new UserResponse();
            userDetail.setId(user.getId());
            userDetail.setUsername(user.getUsername());
            userDetail.setAccountNumber(user.getAccountNumber());
            userDetail.setUsername(user.getUsername());
            userDetail.setEmail(user.getEmail());
            userDetail.setBalance(user.getBalance());
            userDetail.setFirstName(user.getFirstName());
            userDetail.setLastName(user.getLastName());
            userDetail.setDateOfBirth(user.getDateOfBirth());
            userDetail.setRoles(user.getRoles());
            return userDetail;
        }).findFirst();
    }
}
