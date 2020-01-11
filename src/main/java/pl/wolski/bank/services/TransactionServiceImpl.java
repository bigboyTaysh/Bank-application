package pl.wolski.bank.services;


import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.TransactionType;
import pl.wolski.bank.repositories.TransactionRepository;
import pl.wolski.bank.repositories.TransactionTypeRepository;

import java.util.List;


@Service("transactionDetailsService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void save(Transaction transaction) {
        transactionRepository.saveAndFlush(transaction);
    }

}