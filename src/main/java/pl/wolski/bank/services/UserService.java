package pl.wolski.bank.services;


import pl.wolski.bank.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface UserService extends UserDetailsService {
// Własne metody
    void save(User user);

    boolean isUniqueLogin(String login);
    boolean isUniqueEmail(String email);
    boolean isUniquePersonalIdentificationNumber(BigDecimal personalIdentificationNumber);
}
