package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.CreditApplication;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.repositories.CreditApplicationRepository;
import pl.wolski.bank.repositories.CreditTypeRepository;


@Service
public class CreditApplicationServiceImpl implements CreditApplicationService {

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    @Override
    public void save(CreditApplication creditApplication) {
        creditApplicationRepository.saveAndFlush(creditApplication);
    }
}