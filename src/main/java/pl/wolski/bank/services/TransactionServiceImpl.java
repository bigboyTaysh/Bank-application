package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.simulation.MyThread;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.TransactionType;
import pl.wolski.bank.models.User;
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

    @Override
    public boolean save(User user, Transaction transaction) {
        BankAccount bankAccountTo = bankAccountRepository.findByBankAccountNumber(transaction.getToBankAccountNumber());
        BankAccount bankAccountFrom = bankAccountRepository.findByBankAccountNumber(transaction.getFromBankAccountNumber());

        if(bankAccountTo == null){
            if(bankAccountFrom.getAvailableFounds().compareTo(transaction.getValue()) == 0 ||
                    bankAccountFrom.getAvailableFounds().compareTo(transaction.getValue()) == 1){

                bankAccountFrom.setAvailableFounds(bankAccountFrom.getAvailableFounds().subtract(transaction.getValue()));
                bankAccountFrom.setLock(bankAccountFrom.getLock().add(transaction.getValue()));

                bankAccountService.runThread(transaction.getFromBankAccountNumber(), transaction.getValue());

                transaction.setBalanceAfterTransactionUserFrom(bankAccountFrom.getBalance().subtract(transaction.getValue()));
                transaction.setUserNameFrom(user.getFirstName() + " " + user.getLastName());
                TransactionType transactionType = transactionTypeRepository.findTransactionTypeByType(TransactionType.Types.TRANSFER);
                transaction.setTransactionType(transactionType);

                Timestamp stamp = new Timestamp(System.currentTimeMillis());
                Date date = new Date(stamp.getTime());
                transaction.setDate(date);

                bankAccountRepository.save(bankAccountFrom);
                transactionRepository.saveAndFlush(transaction);

                return true;
            } else {
                return false;
            }
        } else {
            if(bankAccountFrom.getAvailableFounds().compareTo(transaction.getValue()) == 0 ||
                    bankAccountFrom.getAvailableFounds().compareTo(transaction.getValue()) == 1) {
                bankAccountFrom.setBalance(bankAccountFrom.getBalance().subtract(transaction.getValue()));
                bankAccountFrom.setAvailableFounds(bankAccountFrom.getAvailableFounds().subtract(transaction.getValue()));

                bankAccountTo.setBalance(bankAccountTo.getBalance().add(transaction.getValue()));
                bankAccountTo.setAvailableFounds(bankAccountTo.getAvailableFounds().add(transaction.getValue()));

                transaction.setBalanceAfterTransactionUserFrom(bankAccountFrom.getBalance());
                transaction.setBalanceAfterTransactionUserTo(bankAccountTo.getBalance());
                transaction.setUserNameFrom(user.getFirstName() + " " + user.getLastName());
                TransactionType transactionType = transactionTypeRepository.findTransactionTypeByType(TransactionType.Types.TRANSFER);
                transaction.setTransactionType(transactionType);

                Timestamp stamp = new Timestamp(System.currentTimeMillis());
                Date date = new Date(stamp.getTime());
                transaction.setDate(date);

                bankAccountRepository.save(bankAccountTo);
                bankAccountRepository.save(bankAccountFrom);
                transactionRepository.saveAndFlush(transaction);

                return true;
            } else {
                return false;
            }
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