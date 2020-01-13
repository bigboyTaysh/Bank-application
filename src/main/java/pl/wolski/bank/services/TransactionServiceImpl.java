package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.TransactionType;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.BankAccountRepository;
import pl.wolski.bank.repositories.TransactionRepository;
import pl.wolski.bank.repositories.TransactionTypeRepository;
import pl.wolski.bank.repositories.UserRepository;


@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Override
    public void save(User user, Transaction transaction) {
        BankAccount bankAccountTo = bankAccountRepository.findByBankAccountNumber(transaction.getToBankAccountNumber());
        BankAccount bankAccountFrom = bankAccountRepository.findByBankAccountNumber(transaction.getFromBankAccountNumber());

        bankAccountTo.setBalance(bankAccountTo.getBalance().add(transaction.getValue()));
        bankAccountTo.setAvailableFounds(bankAccountTo.getAvailableFounds().add(transaction.getValue()));

        bankAccountFrom.setBalance(bankAccountFrom.getBalance().subtract(transaction.getValue()));
        bankAccountFrom.setAvailableFounds(bankAccountFrom.getAvailableFounds().subtract(transaction.getValue()));

        transaction.setBalanceAfterTransactionUserFrom(bankAccountFrom.getBalance());
        transaction.setBalanceAfterTransactionUserTo(bankAccountTo.getBalance());
        transaction.setUserNameFrom(user.getFirstName() + " " + user.getLastName());
        TransactionType transactionType = transactionTypeRepository.findTransactionTypeByType(TransactionType.Types.TRANSFER);
        transaction.setTransactionType(transactionType);

        bankAccountRepository.save(bankAccountTo);
        bankAccountRepository.save(bankAccountFrom);
        transactionRepository.saveAndFlush(transaction);
    }

}