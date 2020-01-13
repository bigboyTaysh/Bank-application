package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByBankAccountNumber(BigDecimal bankAccountNumber);
    BankAccount findTopByOrderByIdDesc();

    List<BankAccount> findAllByUsers(User user);
}
