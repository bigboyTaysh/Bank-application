package pl.wolski.bank.services;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import pl.wolski.bank.BankApplication;
import pl.wolski.bank.models.RecurringPayment;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;

import java.util.Date;

//import static org.springframework.web.context.support.SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext;

@Log4j2
@Service
public class RecurringPaymentSchedulerJob implements Job {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecurringPaymentService recurringPaymentService;

    private int count = 0;

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRecurringPaymentService(RecurringPaymentService recurringPaymentService) {
        this.recurringPaymentService = recurringPaymentService;
    }

    @Override
    @SneakyThrows
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //processInjectionBasedOnCurrentContext(this);

        Long idRecurringPayment = (Long) context.getJobDetail()
                .getJobDataMap()
                .get("idRecurringPayment");

        Long idUser = (Long) context.getJobDetail()
                .getJobDataMap()
                .get("idUser");

        Integer howManyTimes = (Integer) context.getJobDetail()
                .getJobDataMap()
                .get("howManyTimes");

        Long longVar = 12L;

        recurringPaymentService = (RecurringPaymentService) context.getJobDetail()
                .getJobDataMap()
                .get("recurringPaymentService");

        userService = (UserService) context.getJobDetail()
                .getJobDataMap()
                .get("userService");

        transactionService = (TransactionService) context.getJobDetail()
                .getJobDataMap()
                .get("transactionService");

        RecurringPayment recurringPaymentInRepo = recurringPaymentService.getRecurringPaymentById(idRecurringPayment);

        User user = userService.findById(idUser);

        Transaction transaction = new Transaction();
        transaction.setFromBankAccountNumber(recurringPaymentInRepo.getFromBankAccountNumber());
        transaction.setToBankAccountNumber(recurringPaymentInRepo.getToBankAccountNumber());
        transaction.setValue(recurringPaymentInRepo.getValue());
        transaction.setCurrency(recurringPaymentInRepo.getCurrency());
        transaction.setUserNameTo(recurringPaymentInRepo.getUserNameTo());
        transaction.setTitle(recurringPaymentInRepo.getTitle());

        if (transactionService.isTransferPossible(user, transaction) && recurringPaymentInRepo.getIsActive() && count <= howManyTimes) {
            log.info("Udał się przelew! - " + new Date());
            transactionService.doCashTransfer(user, transaction);
            count++;
        } else {
            log.info("Brak środków na koncie! - " + new Date());
            recurringPaymentService.save(recurringPaymentInRepo);
            context.getScheduler().shutdown();
        }

        // Say Hello to the World and display the date /time

    }
}
