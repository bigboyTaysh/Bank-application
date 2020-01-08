package pl.wolski.bank.services;


import org.springframework.security.core.userdetails.UserDetailsService;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;

public interface AddressService {
// Własne metody
    void save(Address address);

    Address findExistAddress(Address address);
}
