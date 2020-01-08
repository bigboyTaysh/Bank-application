package pl.wolski.bank.repositories;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByApartmentNumberAndCityAndHouseNumberAndStreetAndZipCode(Address address);
}
