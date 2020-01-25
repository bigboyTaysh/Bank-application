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
    public void save(Investment investment, String username) {

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        investment.setStartDate(date);
        investment.setUser(userService.findByUsername(username));

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, investment.getNumberOfMonths());
        Date endDate = cal.getTime();
        investment.setEndDate(endDate);

        investmentRepository.saveAndFlush(investment);


    }

    @Override
    public boolean cashCheck(String username, BigDecimal investmentAmount) {

        User user = userService.findByUsername(username);
        List<BankAccount> bankAccount = bankAccountService.findUserAccounts(user);

        for(int i=0; i<= bankAccount.size(); i++){
            BigDecimal sum = bankAccount.get(i).getBalance();
            if(sum.compareTo(investmentAmount) == -1){
                return false;
            }
        }

        return true;
    }

    @Override
    public List<Investment> findByUser(User user){
        return investmentRepository.findByUser(user);
    }

    @Override
    public boolean payOffTheInvestment(Long id, BigDecimal monthRepayment, User user) {
        return false;
    }


}
