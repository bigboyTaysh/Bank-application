package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.models.Currency;
import pl.wolski.bank.repositories.CreditTypeRepository;
import pl.wolski.bank.repositories.CurrencyRepository;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
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
    public BigDecimal currencyExchange(String currencyFrom, String currencyTo, BigDecimal value){
        return ((currencyRepository.findByName(currencyTo)) .getPurchase().multiply(value))
                .divide(currencyRepository.findByName(currencyFrom).getSale());
    }

    @Override
    public Currency findByName(String name){
        return currencyRepository.findByName(name);
    }
}