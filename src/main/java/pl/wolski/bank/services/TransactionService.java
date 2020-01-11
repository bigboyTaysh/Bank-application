package pl.wolski.bank.services;


import org.hibernate.Transaction;

import java.util.List;

public interface TransactionService {
// Własne metody
    void save(Transaction transaction);
}
