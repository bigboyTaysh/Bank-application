package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.RecurringPayment;
import pl.wolski.bank.repositories.AddressRepository;
import pl.wolski.bank.repositories.RecurringPaymentRepository;
import pl.wolski.bank.repositories.UserRepository;


@Service
public class RecurringPaymentServiceImpl implements RecurringPaymentService {

    @Autowired
    private RecurringPaymentRepository recurringPaymentRepository;

    @Override
    public void save(RecurringPayment recurringPayment) {
        recurringPaymentRepository.saveAndFlush(recurringPayment);
    }
}