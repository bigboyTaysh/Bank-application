package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.models.Currency;
import pl.wolski.bank.repositories.CreditTypeRepository;
import pl.wolski.bank.repositories.CurrencyRepository;

import java.util.List;


@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public void save(Currency currency) {
        currencyRepository.saveAndFlush(currency);
    }

    @Override
    public List<Currency> findAll(){
        return currencyRepository.findAll();
    }
}