package pl.wolski.bank.services;


import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
// Własne metody
    void save(Transaction transaction);

    boolean doCashTransfer(User user, Transaction transaction);

    List<Transaction> findUserTop5Transactions(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber);
    List<Transaction> findUserTransactions(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber);
}
