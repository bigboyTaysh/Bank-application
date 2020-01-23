package pl.wolski.bank.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.wolski.bank.controllers.commands.UserSpecifications;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPersonalIdentificationNumber(BigDecimal personalIdentificationNumber);

    User findByBankAccounts(BankAccount bankAccount);

    Page<User> findAll(Specification specifications, Pageable pageable);
}