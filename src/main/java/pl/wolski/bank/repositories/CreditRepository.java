package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {

}
