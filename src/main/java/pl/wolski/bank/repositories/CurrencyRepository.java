package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.models.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByName(String name);
}
