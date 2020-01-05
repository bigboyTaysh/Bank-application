package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;

public interface AddressRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPersonalIdentificationNumber(BigDecimal personalIdentificationNumber);
}
