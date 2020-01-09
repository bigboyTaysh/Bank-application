package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.AccountType;

public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {

}
