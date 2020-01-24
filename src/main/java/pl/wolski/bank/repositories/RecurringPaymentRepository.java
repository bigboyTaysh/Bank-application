package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.RecurringPayment;

public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, Long> {
}
