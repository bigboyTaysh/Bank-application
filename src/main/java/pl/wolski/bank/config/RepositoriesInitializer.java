package pl.wolski.bank.config;

import pl.wolski.bank.models.*;
import pl.wolski.bank.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.wolski.bank.services.CurrencyService;
import pl.wolski.bank.services.InvestmentTypeService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private CreditTypeRepository creditTypeRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private InvestmentTypeService investmentTypeService;

    @Bean
    InitializingBean init() {
        return () -> {
            if (roleRepository.findAll().isEmpty() == true) {
                try {
                    CreditType creditType = new CreditType("kredyt gotówkowy", new BigDecimal(8.99), new BigDecimal(0.0));
                    CreditType creditType2 = new CreditType("kredyt hipoteczny", new BigDecimal(2.99), new BigDecimal(0.0));
                    creditTypeRepository.save(creditType);
                    creditTypeRepository.save(creditType2);

                    InvestmentType investmentType = new InvestmentType("typ 1", new BigDecimal(2.0));
                    InvestmentType investmentType2 = new InvestmentType("typ 2", new BigDecimal(4.0));
                    investmentTypeService.save(investmentType);
                    investmentTypeService.save(investmentType2);


                    Currency currency = new Currency();
                    currency.setName("PLN");
                    currency.setPurchase(new BigDecimal("1.0000"));
                    currency.setSale(new BigDecimal("1.0000"));
                    currencyService.save(currency);

                    Currency currency2 = new Currency();
                    currency2.setName("EUR");
                    currency2.setPurchase(new BigDecimal("4.295"));
                    currency2.setSale(new BigDecimal("4.2533"));
                    currencyService.save(currency2);

                    AccountType accountType = new AccountType(AccountType.Types.PAY_ACC_FOR_ADULT);
                    AccountType accountType2 = new AccountType(AccountType.Types.PAY_ACC_FOR_YOUNG);
                    AccountType accountType3 = new AccountType(AccountType.Types.FOR_CUR_ACC);

                    accountType.setCommission(new BigDecimal("5.0"));
                    accountType2.setCommission(new BigDecimal("0"));
                    accountType3.setCommission(new BigDecimal("0"));

                    accountTypeRepository.save(accountType);
                    accountTypeRepository.save(accountType2);
                    accountTypeRepository.save(accountType3);

                    Timestamp stamp = new Timestamp(System.currentTimeMillis());
                    Date date = new Date(stamp.getTime());

                    BigDecimal accountNumber = new BigDecimal("11222233334444555566667777");
                    BigDecimal zero = new BigDecimal("0");
                    BankAccount bankAccount = new BankAccount(zero, zero, zero, accountNumber, date, accountType);
                    bankAccount.setCurrency(currency);

                    Role roleUser = roleRepository.save(new Role(Role.Types.ROLE_USER));
                    Role roleAdmin = roleRepository.save(new Role(Role.Types.ROLE_ADMIN));
                    Role roleEmployee = roleRepository.save(new Role(Role.Types.ROLE_EMPLOYEE));

                    transactionTypeRepository.save(new TransactionType(TransactionType.Types.TRANSFER));
                    transactionTypeRepository.save(new TransactionType(TransactionType.Types.CASH_PAYMENT));
                    transactionTypeRepository.save(new TransactionType(TransactionType.Types.CASH_WITHDRAWAL));

                    Address address = new Address("aasdad","12","2","ddsadsa"   , "08-125");
                    addressRepository.save(address);

                    User user = new User("user", true);
                    user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                    user.setPassword(passwordEncoder.encode("user"));

                    User user2 = new User("user2", true);
                    user2.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                    user2.setPassword(passwordEncoder.encode("user2"));
                    user2.setEmail("email1@wp.pl");
                    user2.setFirstName("Patryk");
                    user2.setLastName("Wolak");
                    user2.setPersonalIdentificationNumber(new BigDecimal("12345678912"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(1997, 6, 18);
                    user2.setBirthDate(calendar.getTime());
                    user2.setAddress(address);
                    user2.setPhone("123456789");
                    userRepository.save(user2);

                    bankAccount.setUser(user2);
                    bankAccountRepository.save(bankAccount);

                    User admin = new User("admin", true);
                    admin.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
                    admin.setPassword(passwordEncoder.encode("admina"));
                    admin.setPhone("123456789");
                    admin.setLastName("Last");
                    admin.setEmail("email@email.pl");
                    admin.setFirstName("First");

                    User emplo = new User("emplo", true);
                    emplo.setRoles(new HashSet<>(Arrays.asList(roleEmployee)));
                    emplo.setPassword(passwordEncoder.encode("employ"));
                    emplo.setPhone("123456789");
                    emplo.setLastName("Last");
                    emplo.setEmail("email@email.pl");
                    emplo.setFirstName("First");

                    User test = new User("useradmin", true);
                    test.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleUser)));
                    test.setPassword(passwordEncoder.encode("useradmin"));
                    test.setPhone("123456789");
                    test.setLastName("Last");
                    test.setEmail("email@email.pl");
                    test.setFirstName("First");

                    userRepository.save(emplo);
                    userRepository.save(admin);
                    userRepository.save(test);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
