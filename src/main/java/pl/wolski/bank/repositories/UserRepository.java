package pl.wolski.bank.repositories;

import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPersonalIdentificationNumber(BigDecimal personalIdentificationNumber);

    User findByBankAccounts(BankAccount bankAccount);
}