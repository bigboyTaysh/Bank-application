package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.InvestmentType;


public interface InvestmentTypeRepository extends JpaRepository<InvestmentType, Long> {
}
