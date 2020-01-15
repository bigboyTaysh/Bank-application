package pl.wolski.bank.services;


import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
// Własne metody
    void save(User user, Transaction transaction);

    List<Transaction> findUserTransactions(BigDecimal fromBankAccountNumber, BigDecimal toBankAccountNumber);
}
