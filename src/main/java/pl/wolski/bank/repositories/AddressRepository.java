package pl.wolski.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
