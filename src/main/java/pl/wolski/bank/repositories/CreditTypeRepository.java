package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.CreditType;

public interface CreditTypeRepository extends JpaRepository<CreditType, Long> {

}
