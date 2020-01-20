package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Digits(integer = 7, fraction = 4)
    private BigDecimal purchase;

    @Digits(integer = 7, fraction = 4)
    private BigDecimal sale;

    @OneToMany(mappedBy = "currency")
    private Set<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "currency")
    private Set<Transaction> transactions;
}
