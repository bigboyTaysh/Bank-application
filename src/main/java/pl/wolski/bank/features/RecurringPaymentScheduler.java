package pl.wolski.bank.features;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.wolski.bank.models.RecurringPayment;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.services.TransactionService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Log4j2
@Component
public class RecurringPaymentScheduler implements Job {
    @Autowired
    private TransactionService transactionService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @SneakyThrows
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Say Hello to the World and display the date /time
        log.info("Hello World! - " + new Date());
        //context.getScheduler().shutdown();
    }

    public void startScheldue(RecurringPayment recurringPayment) throws InterruptedException, SchedulerException {
        Date startDate = recurringPayment.getStartDate();
        Date endDate = recurringPayment.getEndDate();

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail job = JobBuilder.newJob(RecurringPaymentScheduler.class)
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
                        CronScheduleBuilder.cronSchedule("* * " + hour +" "+ day + " * ?")
                                .withMisfireHandlingInstructionDoNothing()
                )
                .endAt(endDate)
                .build();

        sched.scheduleJob(job, trigger);
        Thread.sleep(500);
        sched.start();
    }

    public void startScheldueSimulate(RecurringPayment recurringPayment) throws InterruptedException, SchedulerException {
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

        int r, howManyTimes, countOfMonths = recurringPayment.getNumberOfMonths();
        float pom;

        if (day1 > day2) {
            r = m2 - m1 - 1;
        } else {
            r = m2 - m1;
        }

        pom = r/countOfMonths;

        if(pom < 1){
            howManyTimes = 1;
        } else {
            howManyTimes = (int) Math.floor(pom) + 1;
        }

        System.out.println("Ile razy: " + howManyTimes);

        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        JobDetail job = JobBuilder.newJob(RecurringPaymentScheduler.class)
                .withIdentity("job1", "group1")
                .build();

        Date runTime = DateBuilder.evenMinuteDate(new Date());

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startAt(runTime)
                .withSchedule(simpleSchedule().withIntervalInSeconds(5).withRepeatCount(howManyTimes-1)
                )
                .build();


        sched.scheduleJob(job, trigger);
        Thread.sleep(500);
        sched.start();
    }

}
