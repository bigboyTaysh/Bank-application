package pl.wolski.bank.services;


import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.Role;
import pl.wolski.bank.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
// Własne metody
    void save(User user, Address address);

    User findByUsername(String username);

    User findById(Long id);

    List<Role> findRoleByUser(User user);

    boolean isUniqueLogin(String login);
    boolean isUniqueEmail(String email);
    boolean isUniquePersonalIdentificationNumber(BigDecimal personalIdentificationNumber);
}
