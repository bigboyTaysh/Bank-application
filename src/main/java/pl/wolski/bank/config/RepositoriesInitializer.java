package pl.wolski.bank.config;

import pl.wolski.bank.models.*;
import pl.wolski.bank.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class RepositoriesInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    InitializingBean init() {

        return () -> {

            if (roleRepository.findAll().isEmpty() == true) {
                try {
                    Role roleUser = roleRepository.save(new Role(Role.Types.ROLE_USER));
                    Role roleAdmin = roleRepository.save(new Role(Role.Types.ROLE_ADMIN));

                    User user = new User("user", true);
                    user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                    user.setPassword(passwordEncoder.encode("user"));

                    User user2 = new User("user2", true);
                    user2.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                    user2.setPassword(passwordEncoder.encode("user2"));
                    user2.setEmail("email1@wp.pl");
                    user2.setFirstName("Patryk");
                    user2.setLastName("Wol");
                    user2.setPersonalIdentificationNumber(new BigInteger("123456789"));


                    User admin = new User("admin", true);
                    admin.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
                    admin.setPassword(passwordEncoder.encode("admin"));

                    User test = new User("useradmin", true);
                    test.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleUser)));
                    test.setPassword(passwordEncoder.encode("useradmin"));

                    userRepository.save(user);
                    userRepository.save(user2);
                    userRepository.save(admin);
                    userRepository.save(test);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
