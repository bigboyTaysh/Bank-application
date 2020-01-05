package pl.wolski.bank.config;

import pl.wolski.bank.models.*;
import pl.wolski.bank.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

@Configuration
public class RepositoriesInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;

    @Bean
    InitializingBean init() {

        return () -> {

            if (roleRepository.findAll().isEmpty() == true) {
                try {
                    Role roleUser = roleRepository.save(new Role(Role.Types.ROLE_USER));
                    Role roleAdmin = roleRepository.save(new Role(Role.Types.ROLE_ADMIN));

                    Address address = new Address("a","b","c","d", "e");

                    User user = new User("user", true);
                    user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                    user.setPassword(passwordEncoder.encode("user"));

                    User user2 = new User("user2", true);
                    user2.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                    user2.setPassword(passwordEncoder.encode("user2"));
                    user2.setEmail("email1@wp.pl");
                    user2.setFirstName("Patryk");
                    user2.setLastName("Wolak");
                    user2.setPersonalIdentificationNumber(new BigDecimal("123456789"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(1997, 6, 18);
                    user2.setBirthDate(calendar.getTime());
                    user2.setAccountNumber(new BigDecimal("11222233334444555566667777"));
                    user2.setAddress(address);

                    User admin = new User("admin", true);
                    admin.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
                    admin.setPassword(passwordEncoder.encode("admin"));

                    User test = new User("useradmin", true);
                    test.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleUser)));
                    test.setPassword(passwordEncoder.encode("useradmin"));

                    addressRepository.save(address);
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
