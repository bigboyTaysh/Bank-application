package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import pl.wolski.bank.controllers.commands.UserFilter;
import pl.wolski.bank.controllers.commands.UserSpecifications;
import pl.wolski.bank.exceptions.UserNotFoundException;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Role;
import pl.wolski.bank.repositories.AddressRepository;
import pl.wolski.bank.repositories.RoleRepository;
import pl.wolski.bank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    //bez adnotacji @Transactional sesja jest zamykana po wywołaniu findByUsername, co uniemożliwia dociągnięcie ról, mimo fetch=EAGER.
    //ponadto, musi być włączone zarządzanie transakcjami @EnableTransactionManagement
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        pl.wolski.bank.models.User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return createUserDetails(user);
    }

    private UserDetails createUserDetails(pl.wolski.bank.models.User user) {
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        for (Role role : user.getRoles()){
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getType().toString()));
//        }

        Set<GrantedAuthority> grantedAuthorities =
                user.getRoles().stream().map(//mapowanie Role na GrantedAuthority
                        r -> new SimpleGrantedAuthority(r.getType().toString())
                ).collect(Collectors.toSet());

        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, grantedAuthorities);
    }

    @Override
    public void save(pl.wolski.bank.models.User user, pl.wolski.bank.models.Address address) {

        Role userRole = roleRepository.findRoleByType(Role.Types.ROLE_USER);

        List roles = Arrays.asList(userRole);
        user.setRoles(new HashSet<>(roles));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordConfirm(null);//wyzerowanie jest potrzebne ze względu na walidację adnotacjami hibernate
        user.setAddress(address);

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        user.setJoinDate(date);

        userRepository.saveAndFlush(user);
    }

    @Override
    public pl.wolski.bank.models.User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public pl.wolski.bank.models.User findById(Long id){
        Optional<pl.wolski.bank.models.User> optionalUser = userRepository.findById(id);
        pl.wolski.bank.models.User user = optionalUser.orElseThrow(() -> new UserNotFoundException(id));

        return user;
    }

    @Override
    public pl.wolski.bank.models.User findByBankAccounts(BankAccount bankAccount){
        return userRepository.findByBankAccounts(bankAccount);
    }

    @Override
    public Page<pl.wolski.bank.models.User> getAllUsers(UserFilter search, Pageable pageable, String type) {


        Page page = userRepository.findAll(
                Specification.where(
                        UserSpecifications.findAll(type).and(
                                UserSpecifications.findByPersonalIdentificationNumber(search.getPersonalIdentificationNumber()))
                ), pageable);

        return page;

    }

    @Override
    public Page<pl.wolski.bank.models.User> getAllUsersByTypeAndPhrase(UserFilter search, Pageable pageable, String type) {


        Page page = userRepository.findAll(
                Specification.where(
                        UserSpecifications.findByPhrase(search.getPhrase(), type).and(
                                UserSpecifications.findByPersonalIdentificationNumber(search.getPersonalIdentificationNumber()))
                ), pageable);

        return page;

    }
    /*

    @Override
    public Page<pl.wolski.bank.models.User> findAllUsersUsingFilter(String phrase, String personalIdentificationNumber, Pageable pageable){
        Page page = userRepository.findAllUsersUsingFilter(phrase, personalIdentificationNumber, pageable);
        return page;
    }

     */

    @Override
    public List<Role> findRoleByUser(pl.wolski.bank.models.User user){
        return roleRepository.findByUsers(user);
    }

    @Override
    public boolean isUniqueLogin(String username) {
        return userRepository.findByUsername(username) == null;
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return userRepository.findByEmail(email) == null;
    }

    @Override
    public boolean isUniquePersonalIdentificationNumber(BigDecimal personalIdentificationNumber) {
        return userRepository.findByPersonalIdentificationNumber(personalIdentificationNumber) == null;
    }
}