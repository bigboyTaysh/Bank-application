package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.Credit;
import pl.wolski.bank.models.User;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findByUser(User user);
}
