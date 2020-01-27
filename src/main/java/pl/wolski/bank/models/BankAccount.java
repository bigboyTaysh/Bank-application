package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal balance;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal availableFounds;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal lock;

    @DecimalMin(value = "10000000000000000000000000")
    @DecimalMax(value = "99999999999999999999999999")
    @Digits(integer = 26, fraction = 0)
    private BigDecimal bankAccountNumber;

    @Past
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Temporal(TemporalType.DATE)
    private Date creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_account_type", nullable = false)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_currency", nullable = false)
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_user", nullable = false)
    private User user;

    public BankAccount(BigDecimal balance, BigDecimal availableFounds, BigDecimal lock, BigDecimal bankAccountNumber, Date creationDate, AccountType accountType) {
        this.balance = balance;
        this.availableFounds = availableFounds;
        this.lock = lock;
        this.bankAccountNumber = bankAccountNumber;
        this.creationDate = creationDate;
        this.accountType = accountType;
    }
}
