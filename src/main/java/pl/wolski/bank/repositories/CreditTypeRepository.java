package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.CreditType;

import java.util.List;

public interface CreditTypeRepository extends JpaRepository<CreditType, Long> {
}
