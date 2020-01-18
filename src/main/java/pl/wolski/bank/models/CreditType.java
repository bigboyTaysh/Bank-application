package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    private Double creditRates;

    private Double commission;

    public CreditType(String name, double creditRates, double commission) {
        this.name = name;
        this.creditRates = creditRates;
        this.commission = commission;
    }
}
