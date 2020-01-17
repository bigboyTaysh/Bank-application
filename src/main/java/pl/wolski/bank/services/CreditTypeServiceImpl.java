package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.repositories.CreditTypeRepository;



@Service
public class CreditTypeServiceImpl implements CreditTypeService {

    @Autowired
    private CreditTypeRepository creditTypeRepository;

    @Override
    public void save(CreditType creditType) {
        creditTypeRepository.saveAndFlush(creditType);
    }
}