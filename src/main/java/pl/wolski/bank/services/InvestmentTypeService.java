package pl.wolski.bank.services;

import pl.wolski.bank.models.InvestmentType;

import java.util.List;

public interface InvestmentTypeService {
    // Własne metody
    void save(InvestmentType investmentType);
    List<InvestmentType> findAll();
}
