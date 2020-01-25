package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    private Double investmentRates;

    private Double commission;

    public InvestmentType(String name, double investmentRates, double commission) {
        this.name = name;
        this.investmentRates = investmentRates;
        this.commission = commission;
    }
}
