package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.exceptions.TransactionNotFoundException;
import pl.wolski.bank.exceptions.UserNotFoundException;
import pl.wolski.bank.models.*;
import pl.wolski.bank.repositories.BankAccountRepository;
import pl.wolski.bank.repositories.TransactionRepository;
import pl.wolski.bank.repositories.TransactionTypeRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void doCashWithdrawal(Transaction transaction) {
        BankAccount bankAccountFrom = bankAccountRepository.findByBankAccountNumber(transaction.getFromBankAccountNumber());

        BigDecimal valueWithCommision = transaction.getValue().add(bankAccountFrom.getAccountType().getCommission());

        bankAccountFrom.setAvailableFounds(bankAccountFrom.getAvailableFounds().subtract(valueWithCommision));
        bankAccountFrom.setBalance(bankAccountFrom.getBalance().subtract(valueWithCommision));

        User userFrom = userService.findByBankAccounts(bankAccountFrom);

        Notification notification = new Notification();
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        notification.setDate(date);

        notification.setTitle("Wypłata gotówki");
        notification.setMessage("-" + valueWithCommision + " " + transaction.getCurrency().getName()
                + "\n Na rachunku " + bankAccountFrom.getBankAccountNumber());
        notification.setUser(userFrom);
        notification.setWasRead(false);

        transaction.setBalanceAfterTransactionUserFrom(bankAccountFrom.getBalance());
        transaction.setUserNameFrom(bankAccountFrom.getBankAccountNumber().toString());
        TransactionType transactionType = transactionTypeRepository.findTransactionTypeByType(TransactionType.Types.CASH_WITHDRAWAL);
        transaction.setTransactionType(transactionType);
        transaction.setDate(date);
        transaction.setTitle("Wypłata gotówki w oddziale");

        notificationService.save(notification);
        bankAccountRepository.save(bankAccountFrom);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void doCashPayment(Transaction transaction) {
        BankAccount bankAccountTo = bankAccountRepository.findByBankAccountNumber(transaction.getToBankAccountNumber());

        BigDecimal value = transaction.getValue();

        bankAccountTo.setAvailableFounds(bankAccountTo.getAvailableFounds().add(value));
        bankAccountTo.setBalance(bankAccountTo.getBalance().add(value));

        User userTo = userService.findByBankAccounts(bankAccountTo);

        Notification notification = new Notification();
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        notification.setDate(date);

        notification.setTitle("Wpłata gotówki");
        notification.setMessage("+" + value + transaction.getCurrency().getName()
                + "\n Na rachunku " + bankAccountTo.getBankAccountNumber());
        notification.setUser(userTo);
        notification.setWasRead(false);

        transaction.setBalanceAfterTransactionUserTo(bankAccountTo.getBalance());
        transaction.setUserNameTo(bankAccountTo.getBankAccountNumber().toString());
        TransactionType transactionType = transactionTypeRepository.findTransactionTypeByType(TransactionType.Types.CASH_PAYMENT);
        transaction.setTransactionType(transactionType);
        transaction.setDate(date);
        transaction.setTitle("Wpłata gotówki w oddziale");

        notificationService.save(notification);
        bankAccountRepository.save(bankAccountTo);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public boolean isTransferPossible(User user, Transaction transaction) {
        BankAccount bankAccountFrom = bankAccountRepository.findByBankAccountNumber(transaction.getFromBankAccountNumber());

        BigDecimal availableFounds = bankAccountFrom.getAvailableFounds();
        BigDecimal commission = bankAccountFrom.getAccountType().getCommission();
        BigDecimal value = transaction.getValue();
        BigDecimal valueExchange;

        if (!transaction.getCurrency().getName().equals(bankAccountFrom.getCurrency().getName())) {
            valueExchange = currencyService.currencyExchange(bankAccountFrom.getCurrency().getName(),
                    transaction.getCurrency().getName(), value);
        } else {
            valueExchange = value;
        }

        int compare = availableFounds.compareTo(valueExchange.add(commission));

        if (compare == 0 || compare == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void doCashTransfer(User user, Transaction transaction) {
        BankAccount bankAccountTo = bankAccountRepository.findByBankAccountNumber(transaction.getToBankAccountNumber());
        BankAccount bankAccountFrom = bankAccountRepository.findByBankAccountNumber(transaction.getFromBankAccountNumber());

        BigDecimal availableFounds = bankAccountFrom.getAvailableFounds();
        BigDecimal commission = bankAccountFrom.getAccountType().getCommission();
        BigDecimal value = transaction.getValue();
        BigDecimal valueExchange;

        if (!transaction.getCurrency().getName().equals(bankAccountFrom.getCurrency().getName())) {
            valueExchange = currencyService.currencyExchange(bankAccountFrom.getCurrency().getName(),
                    transaction.getCurrency().getName(), value);
        } else {
            valueExchange = value;
        }

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

        bankAccountFrom.setAvailableFounds(availableFounds.subtract(valueExchange.add(commission)));

        if (bankAccountTo == null) {

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
            int size = value.toString().length();
            notification.setMessage("+" + value.toString().substring(0, size-2) + " " +transaction.getCurrency().getName()
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

        if (bankAccountTo != null) {
            bankAccountRepository.save(bankAccountTo);
        }
        bankAccountRepository.save(bankAccountFrom);
        transactionRepository.saveAndFlush(transaction);

    }

    @Override
    public List<Transaction> findUserTransactions(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber) {
        return transactionRepository.findByFromBankAccountNumberOrToBankAccountNumberOrderByDateDesc(fromBankAccountNumber, toBankAccountNumber);
    }

    @Override
    public List<Transaction> findUserTop5Transactions(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber) {
        return transactionRepository.findTop5ByFromBankAccountNumberOrToBankAccountNumberOrderByDateDesc(fromBankAccountNumber, toBankAccountNumber);
    }

    @Override
    public Transaction findById(Long id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        Transaction transaction = optionalTransaction.orElseThrow(() -> new TransactionNotFoundException(id));

        return transaction;
    }
}