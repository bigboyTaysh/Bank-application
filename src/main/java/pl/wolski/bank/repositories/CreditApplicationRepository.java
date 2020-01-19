package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.CreditApplication;

import java.util.List;

public interface CreditApplicationRepository extends JpaRepository<CreditApplication, Long> {
    List<CreditApplication> findAllByOrderByDateOfSubmissionOfTheApplicationDesc();
}
