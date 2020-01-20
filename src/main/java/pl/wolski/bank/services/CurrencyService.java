package pl.wolski.bank.services;


import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.models.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyService {
// Własne metody
    void save(Currency currency);
    List<Currency> findAll();
    Currency findByName(String name);
    BigDecimal currencyExchange(String currencyFrom, String currencyTo, BigDecimal value);
}
