package pl.wolski.bank.services;


import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.features.RecurringPaymentScheduler;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.RecurringPayment;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.AddressRepository;
import pl.wolski.bank.repositories.RecurringPaymentRepository;
import pl.wolski.bank.repositories.UserRepository;

import java.util.Date;
import java.util.List;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Log4j2
@Service
public class RecurringPaymentServiceImpl implements RecurringPaymentService {

    @Autowired
    private RecurringPaymentRepository recurringPaymentRepository;

    @Override
    public void save(RecurringPayment recurringPayment) throws SchedulerException, InterruptedException {
        RecurringPaymentScheduler recurringPaymentScheduler = new RecurringPaymentScheduler();
        recurringPaymentScheduler.startScheldueSimulate(recurringPayment);
    }

    @Override
    public List<RecurringPayment> findAllUserRecurringPayment(User user){
        return recurringPaymentRepository.findAllByUserOrderByStartDate(user);
    }
}