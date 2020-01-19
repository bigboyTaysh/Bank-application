package pl.wolski.bank.services;


import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.models.Currency;

import java.util.List;

public interface CurrencyService {
// Własne metody
    void save(Currency currency);
    List<Currency> findAll();
}
