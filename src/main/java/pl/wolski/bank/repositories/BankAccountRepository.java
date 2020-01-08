package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

}
