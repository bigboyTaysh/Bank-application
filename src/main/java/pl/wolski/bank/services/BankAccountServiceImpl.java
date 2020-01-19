package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.exceptions.BankAccountNotFoundException;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Currency;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.AccountTypeRepository;
import pl.wolski.bank.repositories.BankAccountRepository;
import pl.wolski.bank.repositories.CurrencyRepository;
import pl.wolski.bank.repositories.UserRepository;
import pl.wolski.bank.simulation.MyThread;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public void save(BankAccount bankAccount, AccountType accountType) {
        accountTypeRepository.saveAndFlush(accountType);
        bankAccountRepository.saveAndFlush(bankAccount);
    }

    @Override
    public void save(BankAccount bankAccount) {
        bankAccountRepository.saveAndFlush(bankAccount);
    }

    @Override
    public BankAccount findByBankAccountNumber(BigDecimal bankAccountNumber){
        return findByBankAccountNumber(bankAccountNumber);
    }

    @Override
    public BankAccount newBankAccount(User user, BankAccount bankAccount){
        Currency currency = currencyRepository.findByName("PLN");

        pl.wolski.bank.models.BankAccount bankAccountInRepository =
                bankAccountRepository.findTopByOrderByIdDesc();

        User userInRepo = userRepository.findByUsername(user.getUsername());

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

        BigDecimal zero = new BigDecimal("0");

        bankAccount.setCreationDate(date);
        bankAccount.setBalance(zero);
        bankAccount.setAvailableFounds(zero);
        bankAccount.setLock(zero);
        bankAccount.setBankAccountNumber(
                bankAccountInRepository.getBankAccountNumber().add(new BigDecimal("1")));
        bankAccount.setUser(userInRepo);
        bankAccount.setCurrency(currency);

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
    public List<BankAccount> findUserAccounts(User user){
        return bankAccountRepository.findAllByUser(user);
    }

    public void runThread(BigDecimal fromBankAccountNumber, BigDecimal value) {
        Runnable r = new Runnable(fromBankAccountNumber, value);

        Thread t = new Thread(r);
        t.start();
    }

    public class Runnable implements java.lang.Runnable {

        private BigDecimal fromBankAccountNumber, value;

        public Runnable(BigDecimal fromBankAccountNumber, BigDecimal value) {
            this.fromBankAccountNumber = fromBankAccountNumber;
            this.value = value;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Long duration = (long) (10);

                    log.info("Running Task!");

                    TimeUnit.SECONDS.sleep(duration);
                    BankAccount bankAccountInRepository = bankAccountRepository.findByBankAccountNumber(fromBankAccountNumber);

                    bankAccountInRepository.setBalance(bankAccountInRepository.getBalance().subtract(value));
                    bankAccountInRepository.setLock(bankAccountInRepository.getLock().subtract(value));
                    bankAccountRepository.save(bankAccountInRepository);

                    log.info("End task!");
                    Thread.currentThread().interrupt();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

        }
    }
}