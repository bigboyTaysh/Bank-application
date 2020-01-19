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
@Table(name = "credits_applications")
public class CreditApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal creditAmount;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal totalRepayment;

    @Digits(integer = 17, fraction = 4)
    private BigDecimal monthRepayment;

    private int numberOfMonths;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_credit_type", nullable = false)
    private CreditType creditType;

    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Temporal(TemporalType.DATE)
    private Date dateOfSubmissionOfTheApplication;

    private Boolean accepted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_user", nullable = false)
    private User user;
}
