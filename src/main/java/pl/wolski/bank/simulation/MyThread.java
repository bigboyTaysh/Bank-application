package pl.wolski.bank.simulation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import pl.wolski.bank.models.BankAccount;

import pl.wolski.bank.repositories.BankAccountRepository;
import pl.wolski.bank.services.BankAccountService;


import javax.annotation.Resource;
import java.math.BigDecimal;

import java.util.concurrent.TimeUnit;

@Log4j2
public class MyThread {
    /*
    public void runThread(BigDecimal fromBankAccountNumber, BigDecimal value) {
        Runnable r = new Runnable(fromBankAccountNumber, value);

        Thread t = new Thread(r);
        t.start();
    }

    public class Runnable implements java.lang.Runnable {

        private BigDecimal fromBankAccountNumber, value;

        public Runnable(BigDecimal fromBankAccountNumber, BigDecimal value) {
            this.fromBankAccountNumber = fromBankAccountNumber;
            this.value = value;
            log.info("przypisano: " + fromBankAccountNumber + ' ' + value);
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Long duration = (long) (10);

                    log.info("Running Task!");
                    TimeUnit.SECONDS.sleep(duration);
                    bankAccountService.simulate(fromBankAccountNumber, value);
                    log.info("End task!");
                    Thread.currentThread().interrupt();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

        }
    }
     */
}

