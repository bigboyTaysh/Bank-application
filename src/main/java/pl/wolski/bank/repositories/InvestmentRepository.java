package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.Investment;
import pl.wolski.bank.models.User;

import java.util.List;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByUser(User user);
}
