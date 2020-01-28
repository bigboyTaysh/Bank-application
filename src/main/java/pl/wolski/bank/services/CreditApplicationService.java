package pl.wolski.bank.services;


import org.springframework.web.multipart.MultipartFile;
import pl.wolski.bank.models.CreditApplication;
import pl.wolski.bank.models.User;

import java.util.List;

public interface CreditApplicationService {
// Własne metody
    void save(CreditApplication creditApplication, String username, MultipartFile file);
    List<CreditApplication> findAll();
    void updateCreditApplicationStatus(Long id, boolean status);
    CreditApplication getById(Long id);
    List<CreditApplication> findAllByUser(User user);
}
