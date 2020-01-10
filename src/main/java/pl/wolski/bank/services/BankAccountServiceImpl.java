package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.exceptions.BankAccountNotFoundException;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.AccountTypeRepository;
import pl.wolski.bank.repositories.AddressRepository;
import pl.wolski.bank.repositories.BankAccountRepository;
import pl.wolski.bank.repositories.UserRepository;

import java.util.Optional;


@Service("bankAccountDetailsService")
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Override
    public void save(BankAccount bankAccount, AccountType accountType) {
        accountTypeRepository.saveAndFlush(accountType);
        bankAccountRepository.saveAndFlush(bankAccount);
    }

    @Override
    public BankAccount newBankAccount(BankAccount bankAccount){

        pl.wolski.bank.models.BankAccount bankAccountInRepository =
                bankAccountRepository.findByBankAccountNumber(bankAccount.getBankAccountNumber());

        return bankAccount;
    }

    @Override
    public BankAccount getUserAccount (User user){
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(user.getBankAccounts().stream().findFirst().get().getId());
        BankAccount bankAccount = optionalBankAccount.orElseThrow(() -> new BankAccountNotFoundException(user.getBankAccounts().stream().findFirst().get().getId()));
        return bankAccount;
    }
}