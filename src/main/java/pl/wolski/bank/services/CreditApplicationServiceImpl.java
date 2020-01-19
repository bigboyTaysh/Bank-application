package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.exceptions.UserNotFoundException;
import pl.wolski.bank.models.*;
import pl.wolski.bank.repositories.CreditApplicationRepository;
import pl.wolski.bank.repositories.CreditRepository;
import pl.wolski.bank.repositories.CreditTypeRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class CreditApplicationServiceImpl implements CreditApplicationService {

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Override
    public void save(CreditApplication creditApplication, String username) {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        creditApplication.setDateOfSubmissionOfTheApplication(date);

        creditApplication.setUser(userService.findByUsername(username));


        creditApplicationRepository.saveAndFlush(creditApplication);
    }

    @Override
    public List<CreditApplication> findAll(){
        return creditApplicationRepository.findAll();
    }

    @Override
    public void updateCreditApplicationStatus(Long id, boolean status){
        Optional<CreditApplication> optionalCreditApplication = creditApplicationRepository.findById(id);
        CreditApplication creditApplication = optionalCreditApplication.orElseThrow(() -> new UserNotFoundException(id));

        creditApplication.setAccepted(status);
        creditApplicationRepository.save(creditApplication);

        if(status){
            Credit credit = new Credit();
            credit.setCreditAmount(creditApplication.getCreditAmount());
            credit.setTotalRepayment(creditApplication.getTotalRepayment());
            credit.setCurrentRepayment(new BigDecimal("0"));
            credit.setMonthRepayment(creditApplication.getMonthRepayment());
            credit.setNumberOfMonths(creditApplication.getNumberOfMonths());
            credit.setNumberOfMonthsToTheEnd(creditApplication.getNumberOfMonths());

            Date date = new Date(System.currentTimeMillis());
            credit.setStartDate(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, creditApplication.getNumberOfMonths());
            date = cal.getTime();
            credit.setEndDate(date);

            credit.setIsPaidOff(false);
            credit.setCreditType(creditApplication.getCreditType());

            User user = userService.findById(creditApplication.getUser().getId());
            credit.setUser(user);

            BankAccount bankAccount = bankAccountService.getUserAccount(user);
            bankAccount.setBalance(bankAccount.getBalance().add(creditApplication.getCreditAmount()));
            bankAccount.setAvailableFounds(bankAccount.getAvailableFounds().add(creditApplication.getCreditAmount()));

            bankAccountService.save(bankAccount);
            creditRepository.save(credit);
        }
    }

}