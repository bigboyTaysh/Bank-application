package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.models.Currency;
import pl.wolski.bank.repositories.CreditTypeRepository;
import pl.wolski.bank.repositories.CurrencyRepository;

import java.math.BigDecimal;
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

    @Override
    public BigDecimal currencyExchange(Currency currencyFrom, Currency currencyTo, BigDecimal value){
        return ((currencyRepository.findByName(currencyFrom.getName())).getPurchase()
                .divide(currencyRepository.findByName(currencyTo.getName()).getSale()))
                .multiply(value);
    }

    @Override
    public Currency findByName(String name){
        return currencyRepository.findByName(name);
    }
}