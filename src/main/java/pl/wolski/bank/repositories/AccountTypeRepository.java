package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Role;

public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    AccountType findAccountTypeByType(AccountType.Types type);
}
