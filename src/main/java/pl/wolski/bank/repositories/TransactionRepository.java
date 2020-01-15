package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromBankAccountNumberOrToBankAccountNumberOrderByDateDesc(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber);
}
