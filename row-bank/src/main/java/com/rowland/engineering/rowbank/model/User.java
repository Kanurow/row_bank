package com.rowland.engineering.rowbank.model;

import com.rowland.engineering.rowbank.model.audit.DateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
@Table(name = "users_table", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "account_number"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 2,max = 25)
    private String firstName;

    @NotBlank
    @Size(min = 2,max = 25)
    private String lastName;

    @NotBlank
    @Size(min = 2,max = 20)
    @NaturalId
    private String username;

    @NaturalId
    @NotBlank(message = "User must have an account number")
    @Size(max = 10)
    private String accountNumber;

    @PastOrPresent(message = "Date of birth cannot be in future")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @NaturalId
    @Email(message = "Must be a valid email address")
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BankName bankName;

    @PositiveOrZero(message = "Balance must be 0.0 or greater")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal balance;

    @NotBlank
    private String password;

    private boolean accountLocked;

    private boolean isDeleted;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts = 0;

    @Column(name = "last_login_attempt")
    private LocalDateTime lastLoginAttempt;

    public User(BankName bankName,String firstName, String lastName, LocalDate dateOfBirth,
                String username, String email, String password, BigDecimal balance) {
        this.bankName = bankName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
