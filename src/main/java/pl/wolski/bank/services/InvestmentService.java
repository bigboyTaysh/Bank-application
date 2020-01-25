package pl.wolski.bank.services;

import pl.wolski.bank.models.Investment;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface InvestmentService {
    // Własne metody
    void save(Investment investment, String username);

    boolean cashCheck(String username, BigDecimal investmentAmount);

    List<Investment> findByUser(User user);

    boolean payOffTheInvestment(Long id, BigDecimal monthRepayment, User user);
}
