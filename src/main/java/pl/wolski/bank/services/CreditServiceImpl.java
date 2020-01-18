package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.wolski.bank.exceptions.CreditNotFoundException;
import pl.wolski.bank.exceptions.UserNotFoundException;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Credit;
import pl.wolski.bank.models.CreditApplication;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.CreditRepository;
import pl.wolski.bank.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Override
    public void save(Credit credit) {
        creditRepository.saveAndFlush(credit);
    }

    @Override
    public List<Credit> findByUser(User user){
        return creditRepository.findByUser(user);
    }

    @Override
    public  boolean payOffTheCredit(Long id, BigDecimal monthRepayment, User user){
        Optional<Credit> optionalCredit = creditRepository.findById(id);
        Credit credit = optionalCredit.orElseThrow(() -> new CreditNotFoundException(id));
        int numberOfMonthsToTheEnd = credit.getNumberOfMonthsToTheEnd();
        int monthsToSubtraction = (monthRepayment.divide(credit.getMonthRepayment())).intValueExact();

        BankAccount bankAccount = bankAccountService.getUserAccount(userService.findByUsername(user.getUsername()));

        if (bankAccount.getAvailableFounds().compareTo(monthRepayment) == 0
                || bankAccount.getAvailableFounds().compareTo(monthRepayment) == 1){
            bankAccount.setAvailableFounds(bankAccount.getAvailableFounds().subtract(monthRepayment));
            bankAccount.setBalance(bankAccount.getBalance().subtract(monthRepayment));
            credit.setNumberOfMonthsToTheEnd(numberOfMonthsToTheEnd - monthsToSubtraction);
            Date date = new Date(System.currentTimeMillis());
            credit.setLastPayment(date);

            Calendar c = Calendar.getInstance();
            c.setTime(credit.getEndDate());
            c.add(Calendar.MONTH, -numberOfMonthsToTheEnd);

            credit.setEndDate(c.getTime());

            if (credit.getNumberOfMonthsToTheEnd() == 0) {
                credit.setIsPaidOff(true);
            }

            creditRepository.save(credit);
            bankAccountService.save(bankAccount);

            return true;
        } else {
            return false;
        }
    }
}