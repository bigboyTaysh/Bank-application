package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.repositories.AccountTypeRepository;
import pl.wolski.bank.repositories.AddressRepository;
import pl.wolski.bank.repositories.UserRepository;

import java.util.List;


@Service
public class AccountTypeServiceImpl implements AccountTypeService {

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Override
    public void save(AccountType accountType) {
        accountTypeRepository.saveAndFlush(accountType);
    }

    @Override
    public List<AccountType> getAllTypes() {
        return accountTypeRepository.findAll();
    }
}