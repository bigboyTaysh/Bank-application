package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.Credit;
import pl.wolski.bank.repositories.CreditRepository;


@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Override
    public void save(Credit credit) {
        creditRepository.saveAndFlush(credit);
    }
}