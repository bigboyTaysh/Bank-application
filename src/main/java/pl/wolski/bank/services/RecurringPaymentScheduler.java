package pl.wolski.bank.services;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.RecurringPayment;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.RecurringPaymentRepository;

import java.util.*;
import java.util.Calendar;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Log4j2
@Service
public class RecurringPaymentScheduler {
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

    public void startScheldue(RecurringPayment recurringPayment) throws InterruptedException, SchedulerException {
        Date startDate = recurringPayment.getStartDate();
        Date endDate = recurringPayment.getEndDate();

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail job = JobBuilder.newJob(RecurringPaymentSchedulerJob.class)
                .withIdentity("job1", "group1")
                .build();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Warsaw"));
        cal.setTime(startDate);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(startDate)
                .withSchedule(
                        CronScheduleBuilder.cronSchedule("* * " + hour + " " + day + " * ?")
                                .withMisfireHandlingInstructionDoNothing()
                )
                .endAt(endDate)
                .build();

        sched.scheduleJob(job, trigger);
        Thread.sleep(500);
        sched.start();
    }

    public void startScheldueSimulate(User user, RecurringPayment recurringPayment) throws
            InterruptedException, SchedulerException {
        Date startDate = recurringPayment.getStartDate();
        Date endDate = recurringPayment.getEndDate();

        Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("Europe/Warsaw"));
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("Europe/Warsaw"));
        cal1.setTime(startDate);
        cal2.setTime(endDate);

        int day1 = cal1.get(Calendar.DAY_OF_MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);
        int month1 = cal1.get(Calendar.MONTH);
        int month2 = cal2.get(Calendar.MONTH);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);

        int m1 = year1 * 12 + month1;
        int m2 = year2 * 12 + month2;

        int r, countOfMonths = recurringPayment.getNumberOfMonths(), howManyTimes;
        float pom;

        if (day1 > day2) {
            r = m2 - m1 - 1;
        } else {
            r = m2 - m1;
        }

        pom = r / countOfMonths;

        if (pom < 1) {
            howManyTimes = 0;
        } else {
            howManyTimes = (int) Math.floor(pom);
        }

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("howManyTimes", howManyTimes);
        jobDataMap.put("idUser", user.getId());
        jobDataMap.put("idRecurringPayment", recurringPayment.getId());
        jobDataMap.put("userService", userService);
        jobDataMap.put("transactionService", transactionService);
        jobDataMap.put("recurringPaymentService", recurringPaymentService);

        JobDetail job = JobBuilder.newJob(RecurringPaymentSchedulerJob.class)
                .withIdentity("job1", "group1")
                .usingJobData(jobDataMap)
                .build();


        Date runTime = DateBuilder.evenMinuteDate(new Date());

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runTime)
                .withSchedule(simpleSchedule().withIntervalInSeconds(5).withRepeatCount(howManyTimes)
                )
                .build();


        sched.scheduleJob(job, trigger);
        Thread.sleep(500);
        sched.start();
    }

}
