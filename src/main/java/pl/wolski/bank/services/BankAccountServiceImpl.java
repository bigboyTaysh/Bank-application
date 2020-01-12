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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


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
                bankAccountRepository.findTopByOrderByIdDesc();

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

        BigDecimal zero = new BigDecimal("0");

        bankAccount.setCreationDate(date);
        bankAccount.setBalance(zero);
        bankAccount.setAvailableFounds(zero);
        bankAccount.setLock(zero);
        bankAccount.setBankAccountNumber(
                bankAccountInRepository.getBankAccountNumber().add(new BigDecimal("1")));

        bankAccountRepository.save(bankAccount);

        return bankAccount;
    }

    @Override
    public BankAccount getUserAccount (User user){
        Optional<BankAccount> optionalBankAccount =
                bankAccountRepository.findById(
                        user.getBankAccounts().stream().min(Comparator.comparing(BankAccount::getCreationDate)).get().getId());
        BankAccount bankAccount = optionalBankAccount.orElseThrow(()
                -> new BankAccountNotFoundException(
                        user.getBankAccounts().stream().min(Comparator.comparing(BankAccount::getCreationDate)).get().getId()));

        return bankAccount;
    }

    @Override
    public List<BankAccount> findByUsers_Id(Long id){
        return findByUsers_Id(id);
    };
}