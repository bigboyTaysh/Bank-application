package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.TransactionType;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
    TransactionType findTransactionTypeByType(TransactionType.Types types);
}
