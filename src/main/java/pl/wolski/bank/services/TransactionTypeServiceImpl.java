package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.TransactionType;
import pl.wolski.bank.repositories.AccountTypeRepository;
import pl.wolski.bank.repositories.TransactionTypeRepository;

import java.util.List;


@Service
public class TransactionTypeServiceImpl implements TransactionTypeService {

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Override
    public void save(TransactionType transactionType) {
        transactionTypeRepository.saveAndFlush(transactionType);
    }
}