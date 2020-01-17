package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "credits")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal creditAmount;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal totalRepayment;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal currentRepayment;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal monthRepayment;

    private int numberOfMonths;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date lastPayment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_credit_type", nullable = true)
    private CreditType creditType;
}
