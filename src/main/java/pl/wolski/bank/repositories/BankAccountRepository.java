package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.wolski.bank.models.*;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByBankAccountNumber(BigDecimal bankAccountNumber);
    BankAccount findTopByOrderByIdDesc();
    BankAccount findByUserAndCurrency(User user, Currency currency);

    List<BankAccount> findAllByUser(User user);
    List<BankAccount> findAllByUserOrderByCreationDate(User user);
}
