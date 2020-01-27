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

    @Autowired
    private NotificationService notificationService;

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
        return creditApplicationRepository.findAllByOrderByDateOfSubmissionOfTheApplicationDesc();
    }

    @Override
    public void updateCreditApplicationStatus(Long id, boolean isAccepted){
        Optional<CreditApplication> optionalCreditApplication = creditApplicationRepository.findById(id);
        CreditApplication creditApplication = optionalCreditApplication.orElseThrow(() -> new UserNotFoundException(id));

        creditApplication.setAccepted(isAccepted);
        creditApplicationRepository.save(creditApplication);

        if(isAccepted){
            Credit credit = new Credit();
            credit.setCreditAmount(creditApplication.getCreditAmount());
            credit.setTotalRepayment(creditApplication.getTotalRepayment());
            credit.setCurrentRepayment(new BigDecimal("0"));
            credit.setMonthRepayment(creditApplication.getMonthRepayment());
            credit.setNumberOfMonths(creditApplication.getNumberOfMonths());
            credit.setNumberOfMonthsToTheEnd(creditApplication.getNumberOfMonths());

            Date currDate = new Date(System.currentTimeMillis());

            Calendar cal = Calendar.getInstance();
            cal.setTime(currDate);
            cal.add(Calendar.MONTH, creditApplication.getNumberOfMonths());
            Date endDate = cal.getTime();
            credit.setStartDate(currDate);
            credit.setEndDate(endDate);

            credit.setIsPaidOff(false);
            credit.setCreditType(creditApplication.getCreditType());

            User user = userService.findById(creditApplication.getUser().getId());
            credit.setUser(user);

            BankAccount bankAccount = bankAccountService.getUserAccount(user);
            bankAccount.setBalance(bankAccount.getBalance().add(creditApplication.getCreditAmount()));
            bankAccount.setAvailableFounds(bankAccount.getAvailableFounds().add(creditApplication.getCreditAmount()));

            Notification notification = new Notification();
            notification.setDate(currDate);
            notification.setTitle("Wniosek o kredyt zaakceptopowano");
            notification.setMessage("Uznanie: +" + creditApplication.getCreditAmount().toString().substring(0, creditApplication.getCreditAmount().toString().length()) + bankAccount.getCurrency().getName()
                    + "\n Na rachunku " + bankAccount.getBankAccountNumber());
            notification.setUser(user);
            notification.setWasRead(false);

            notificationService.save(notification);

            bankAccountService.save(bankAccount);
            creditRepository.save(credit);
        }
    }

}