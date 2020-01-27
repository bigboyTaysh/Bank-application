package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recurring_payments")
public class RecurringPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 17, fraction = 4)
    private BigDecimal value;

    @Digits(integer = 26, fraction = 0)
    private BigDecimal fromBankAccountNumber;

    @NotNull
    @DecimalMin(value = "10000000000000000000000000")
    @DecimalMax(value = "99999999999999999999999999")
    @Digits(integer = 26, fraction = 0)
    private BigDecimal toBankAccountNumber;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    private String userNameTo;

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @NotNull
    @PositiveOrZero
    private int numberOfMonths;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    private Boolean isActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_urrency", nullable = false)
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_user", nullable = false)
    private User user;
}
