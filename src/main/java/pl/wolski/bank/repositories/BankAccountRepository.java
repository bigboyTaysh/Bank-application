package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.BankAccount;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByBankAccountNumber(BigDecimal bankAccountNumber);
    BankAccount findTopByOrderByIdDesc();
    List<BankAccount> findByUsers_Id(Long id);
}
