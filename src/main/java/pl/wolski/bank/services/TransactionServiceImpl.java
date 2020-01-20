package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.*;
import pl.wolski.bank.simulation.MyThread;
import pl.wolski.bank.repositories.BankAccountRepository;
import pl.wolski.bank.repositories.TransactionRepository;
import pl.wolski.bank.repositories.TransactionTypeRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserService userService;

    @Override
    public void save(Transaction transaction){
        transactionRepository.save(transaction);
    }

    @Override
    public boolean doCashTransfer(User user, Transaction transaction) {
        BankAccount bankAccountTo = bankAccountRepository.findByBankAccountNumber(transaction.getToBankAccountNumber());
        BankAccount bankAccountFrom = bankAccountRepository.findByBankAccountNumber(transaction.getFromBankAccountNumber());

        BigDecimal availableFounds = bankAccountFrom.getAvailableFounds();
        BigDecimal commission = bankAccountFrom.getAccountType().getCommission();
        BigDecimal value = transaction.getValue();
        BigDecimal valueExchange;

        if (!transaction.getCurrency().getName().equals(bankAccountFrom.getCurrency().getName())){
            valueExchange = currencyService.currencyExchange(bankAccountFrom.getCurrency().getName(),
                    transaction.getCurrency().getName(), value);
        } else {
            valueExchange = value;
        }

        int compareTo = availableFounds.compareTo(valueExchange.add(commission));

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

        if(compareTo == 0 || compareTo == 1){
            bankAccountFrom.setAvailableFounds(availableFounds.subtract(valueExchange.add(commission)));

            if(bankAccountTo == null){

                bankAccountFrom.setLock(bankAccountFrom.getLock().add(valueExchange.add(commission)));

                bankAccountService.runThread(transaction.getFromBankAccountNumber(), valueExchange.add(commission));
            } else {
                bankAccountFrom.setBalance(bankAccountFrom.getBalance().subtract(valueExchange.add(commission)));

                bankAccountTo.setBalance(bankAccountTo.getBalance().add(value));
                bankAccountTo.setAvailableFounds(bankAccountTo.getAvailableFounds().add(value));
                transaction.setBalanceAfterTransactionUserTo(bankAccountTo.getBalance());

                User userTo = userService.findByBankAccounts(bankAccountTo);

                Notification notification = new Notification();
                notification.setDate(date);
                notification.setTitle("Uznanie rachunku");
                notification.setMessage("+" + value + transaction.getCurrency().getName()
                        + "\n Na rachunku " + bankAccountTo.getBankAccountNumber());
                notification.setUser(userTo);
                notification.setWasRead(false);
                notificationService.save(notification);
            }

            transaction.setBalanceAfterTransactionUserFrom(bankAccountFrom.getBalance());
            transaction.setUserNameFrom(user.getFirstName() + " " + user.getLastName());
            TransactionType transactionType = transactionTypeRepository.findTransactionTypeByType(TransactionType.Types.TRANSFER);
            transaction.setTransactionType(transactionType);
            transaction.setDate(date);

            if(bankAccountTo != null){
                bankAccountRepository.save(bankAccountTo);
            }
            bankAccountRepository.save(bankAccountFrom);
            transactionRepository.saveAndFlush(transaction);

            return true;
        } else {
            log.info("Wynik porównania " + Integer.toString(compareTo));

            return false;
        }
    }

    @Override
    public List<Transaction> findUserTransactions(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber){
        return transactionRepository.findByFromBankAccountNumberOrToBankAccountNumberOrderByDateDesc(fromBankAccountNumber, toBankAccountNumber);
    }

    @Override
    public List<Transaction> findUserTop5Transactions(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber){
        return transactionRepository.findTop5ByFromBankAccountNumberOrToBankAccountNumberOrderByDateDesc(fromBankAccountNumber, toBankAccountNumber);
    }
}