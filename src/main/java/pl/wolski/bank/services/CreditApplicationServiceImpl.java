package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.CreditApplication;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.repositories.CreditApplicationRepository;
import pl.wolski.bank.repositories.CreditTypeRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Service
public class CreditApplicationServiceImpl implements CreditApplicationService {

    @Autowired
    private CreditApplicationRepository creditApplicationRepository;

    @Autowired
    private UserService userService;

    @Override
    public void save(CreditApplication creditApplication, String username) {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        creditApplication.setDateOfSubmissionOfTheApplication(date);

        creditApplication.setUser(userService.findByUsername(username));


        creditApplicationRepository.saveAndFlush(creditApplication);
    }

    @Override
    public List<CreditApplication> findAll(){
        return creditApplicationRepository.findAll();
    }
}