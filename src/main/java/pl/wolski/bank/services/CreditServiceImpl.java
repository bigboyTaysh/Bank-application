package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.Credit;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.CreditRepository;

import java.math.BigDecimal;
import java.util.List;


@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Override
    public void save(Credit credit) {
        creditRepository.saveAndFlush(credit);
    }

    @Override
    public List<Credit> findByUser(User user){
        return creditRepository.findByUser(user);
    }

    @Override
    public  boolean payOffTheCredit(Long id, BigDecimal monthRepayment){
        return true;
    }
}