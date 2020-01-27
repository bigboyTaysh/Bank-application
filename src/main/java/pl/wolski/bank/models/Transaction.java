package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 26, fraction = 0)
    private BigDecimal fromBankAccountNumber;

    @NotBlank
    @NotNull
    @Size(min = 26, max = 26)
    @Digits(integer = 26, fraction = 0)
    private BigDecimal toBankAccountNumber;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 17, fraction = 4)
    private BigDecimal value;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal balanceAfterTransactionUserFrom;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal balanceAfterTransactionUserTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_urrency", nullable = false)
    private Currency currency;

    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Temporal(TemporalType.DATE)
    private Date date;

    private String userNameFrom;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    private String userNameTo;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private TransactionType transactionType;

    public Transaction(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber,
                       BigDecimal value, BigDecimal balanceAfterTransactionUserFrom, BigDecimal balanceAfterTransactionUserTo, Date date, String title,
                       TransactionType transactionType) {
        this.fromBankAccountNumber = fromBankAccountNumber;
        this.toBankAccountNumber = toBankAccountNumber;
        this.value = value;
        this.balanceAfterTransactionUserFrom = balanceAfterTransactionUserFrom;
        this.balanceAfterTransactionUserTo = balanceAfterTransactionUserFrom;
        this.date = date;
        this.title = title;
        this.transactionType = transactionType;
    }

    public Transaction(BigDecimal toBankAccountNumber, BigDecimal value,
                       BigDecimal balanceAfterTransactionUserTo, Date date,
                       TransactionType transactionType) {
        this.toBankAccountNumber = toBankAccountNumber;
        this.value = value;
        this.balanceAfterTransactionUserTo = balanceAfterTransactionUserTo;
        this.date = date;
        this.transactionType = transactionType;
    }
}
