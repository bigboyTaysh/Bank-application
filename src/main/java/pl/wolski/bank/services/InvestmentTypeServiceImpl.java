package pl.wolski.bank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.InvestmentType;
import pl.wolski.bank.repositories.InvestmentTypeRepository;

import java.util.List;

@Service
public class InvestmentTypeServiceImpl implements InvestmentTypeService {

    @Autowired
    private InvestmentTypeRepository investmentTypeRepository;


    @Override
    public void save(InvestmentType investmentType) {
        investmentTypeRepository.saveAndFlush(investmentType);
    }

    @Override
    public List<InvestmentType> findAll() {
        return investmentTypeRepository.findAll();
    }
}
