package pl.wolski.bank.services;


import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;

public interface TransactionService {
// Własne metody
    void save(User user, Transaction transaction);
}
