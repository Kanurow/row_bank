package com.rowland.engineering.rowbank.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
@Table(name = "transactions_table")
@Validated
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private TransactionType transactionType;
    @Min(value = 50)
    private BigDecimal amount;
    @PastOrPresent
    private LocalDateTime timestamp;

    @Size(max = 300)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Valid
    private User user;

    @AssertTrue(message = "Invalid transaction type")
    public boolean isValidTransactionType() {
        return TransactionType.DEBIT.equals(transactionType) || TransactionType.CREDIT.equals(transactionType);
    }
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        return timestamp.format(formatter);
    }
}
