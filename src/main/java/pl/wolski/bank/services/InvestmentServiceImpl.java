package pl.wolski.bank.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Investment;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.CreditRepository;
import pl.wolski.bank.repositories.InvestmentRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class InvestmentServiceImpl implements InvestmentService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Override
    public void save(Investment investment) {

        investmentRepository.save(investment);
    }

    @Override
    public void openInvestment(Investment investment, String username) {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        investment.setStartDate(date);
        investment.setUser(userService.findByUsername(username));

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, investment.getNumberOfMonths());
        Date endDate = cal.getTime();
        investment.setEndDate(endDate);

        User user = userService.findByUsername(username);
        BankAccount bankAccount = bankAccountService.getUserAccount(user);

        bankAccount.setAvailableFounds(bankAccount.getAvailableFounds().subtract(investment.getInvestmentAmount()));
        bankAccount.setBalance(bankAccount.getBalance().subtract(investment.getInvestmentAmount()));

        bankAccountService.save(bankAccount);
        investmentRepository.saveAndFlush(investment);
    }

    @Override
    public boolean enableInvestment(String username, BigDecimal investmentAmount) {
        User user = userService.findByUsername(username);
        BankAccount bankAccount = bankAccountService.getUserAccount(user);
        int compare = bankAccount.getAvailableFounds().compareTo(investmentAmount);

        if (compare == 0 || compare == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Investment> findByUser(User user) {
        return investmentRepository.findByUser(user);
    }

    @Override
    public boolean payOffTheInvestment(Long id, BigDecimal monthRepayment, User user) {
        return false;
    }


}
