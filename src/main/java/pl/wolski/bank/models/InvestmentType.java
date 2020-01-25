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
@Table(name = "investment_types")
public class InvestmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal investmentRates;

    public InvestmentType(String name, BigDecimal investmentRates) {
        this.name = name;
        this.investmentRates = investmentRates;
    }
}
