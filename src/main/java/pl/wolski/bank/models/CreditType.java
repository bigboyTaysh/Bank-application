package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "credit_types")
public class CreditType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal creditRates;

    private BigDecimal commission;

    public CreditType(String name, BigDecimal creditRates, BigDecimal commission) {
        this.name = name;
        this.creditRates = creditRates;
        this.commission = commission;
    }
}
