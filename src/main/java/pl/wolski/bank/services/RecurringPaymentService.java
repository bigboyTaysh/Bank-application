package pl.wolski.bank.services;


import org.quartz.SchedulerException;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.RecurringPayment;
import pl.wolski.bank.models.User;

import java.util.List;

public interface RecurringPaymentService {
// Własne metody
    void save(RecurringPayment recurringPayment);
    void saveWithScheduler(User user, RecurringPayment recurringPayment) throws SchedulerException, InterruptedException;
    List<RecurringPayment> findAllUserRecurringPayment(User user);
    boolean isRecurringPaymentPossible(User user, RecurringPayment recurringPayment);
    RecurringPayment getRecurringPaymentById(Long id);
}

