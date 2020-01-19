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
@Table(name = "account_types")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Digits(integer = 5, fraction = 4)
    private BigDecimal commission;

    @OneToMany(mappedBy = "accountType")
    private Set<BankAccount> bankAccounts;

    @Enumerated(EnumType.STRING)//przechowywane w postaci string
    private AccountType.Types type;

    public AccountType(AccountType.Types type){
        this.type = type;
    }

    public enum Types{
        PAY_ACC_FOR_YOUNG,
        PAY_ACC_FOR_ADULT,
        FOR_CUR_ACC
    }
}
