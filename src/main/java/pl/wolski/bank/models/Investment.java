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
@Table(name = "investments")
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal investmentAmount;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal totalRepayment;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal currentRepayment; //////////////////

    @Digits(integer = 17, fraction = 4)
    private BigDecimal monthRepayment; //////////////////

    private int numberOfMonths;

    private int numberOfMonthsToTheEnd;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date lastPayment; //////////////////

    private Boolean isPaidOff; //////////////////

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_investment_type", nullable = true)
    private InvestmentType investmentType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_user", nullable = false)
    private User user;
}
