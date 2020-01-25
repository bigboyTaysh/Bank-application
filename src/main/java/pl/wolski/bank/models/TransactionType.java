package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transaction_type")
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)//przechowywane w postaci string
    private TransactionType.Types type;

    public TransactionType(TransactionType.Types type){
        this.type = type;
    }

    public enum Types{
        TRANSFER,
        CASH_WITHDRAWAL,
        CASH_PAYMENT
    }
}
