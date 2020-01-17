package pl.wolski.bank.services;


import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface UserService extends UserDetailsService {
// Własne metody
    void save(User user, Address address);

    User findByUsername(String username);

    boolean isUniqueLogin(String login);
    boolean isUniqueEmail(String email);
    boolean isUniquePersonalIdentificationNumber(BigDecimal personalIdentificationNumber);
}
