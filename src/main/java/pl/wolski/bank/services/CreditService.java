package pl.wolski.bank.services;


import pl.wolski.bank.models.Credit;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface CreditService {
// Własne metody
    void save(Credit credit);

    List<Credit> findByUser(User user);

    boolean payOffTheCredit(Long id, BigDecimal monthRepayment, User user);
}
