package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.wolski.bank.exceptions.RecurringPaymentNotFoundException;
import pl.wolski.bank.models.*;
import pl.wolski.bank.repositories.BankAccountRepository;
import pl.wolski.bank.repositories.RecurringPaymentRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class RecurringPaymentServiceImpl implements RecurringPaymentService {

    @Autowired
    private RecurringPaymentRepository recurringPaymentRepository;

    @Autowired
    private RecurringPaymentService recurringPaymentService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserService userService;

    public RecurringPaymentServiceImpl() {
    }


    @Override
    public void save(RecurringPayment recurringPayment){
        recurringPayment.setIsActive(true);
        recurringPaymentRepository.saveAndFlush(recurringPayment);
    }

    @Override
    public void saveWithScheduler(User user, RecurringPayment recurringPayment) throws SchedulerException, InterruptedException {
        RecurringPaymentScheduler recurringPaymentScheduler = new RecurringPaymentScheduler();
        recurringPaymentScheduler.setRecurringPaymentService(recurringPaymentService);
        recurringPaymentScheduler.setUserService(userService);
        recurringPaymentScheduler.setTransactionService(transactionService);
        recurringPaymentScheduler.startScheldueSimulate(user, recurringPayment);
    }

    @Override
    public List<RecurringPayment> findAllUserRecurringPayment(User user){
        return recurringPaymentRepository.findAllByUserOrderByStartDate(user);
    }

    @Override
    public RecurringPayment getRecurringPaymentById(Long id){
        Optional<RecurringPayment> optionalRecurringPayment = recurringPaymentRepository.findById(id);
        RecurringPayment recurringPaymentInRepo = optionalRecurringPayment.orElseThrow(() -> new RecurringPaymentNotFoundException(id));

        return recurringPaymentInRepo;
    }

    @Override
    public boolean isRecurringPaymentPossible(User user, RecurringPayment recurringPayment) {
        BankAccount bankAccountFrom = bankAccountRepository.findByBankAccountNumber(recurringPayment.getFromBankAccountNumber());

        BigDecimal availableFounds = bankAccountFrom.getAvailableFounds();
        BigDecimal commission = bankAccountFrom.getAccountType().getCommission();
        BigDecimal value = recurringPayment.getValue();
        BigDecimal valueExchange;

        if (!recurringPayment.getCurrency().getName().equals(bankAccountFrom.getCurrency().getName())) {
            valueExchange = currencyService.currencyExchange(bankAccountFrom.getCurrency().getName(),
                    recurringPayment.getCurrency().getName(), value);
        } else {
            valueExchange = value;
        }

        int compare = availableFounds.compareTo(valueExchange.add(commission));

        if (compare == 0 || compare == 1) {
            return true;
        } else {
            return false;
        }
    }


}