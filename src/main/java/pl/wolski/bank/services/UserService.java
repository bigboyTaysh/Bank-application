package pl.wolski.bank.services;


import pl.wolski.bank.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
// Własne metody
    void save(User user);

    boolean isUniqueLogin(String login);

    boolean isUniqueEmail(String email);
}
