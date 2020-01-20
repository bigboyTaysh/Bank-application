package pl.wolski.bank.services;


import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Address;

import java.util.List;

public interface AccountTypeService {
// Własne metody
    void save(AccountType accountType);

    AccountType findAccountTypeByType(AccountType.Types type);

    List<AccountType> getAllTypes();
}
