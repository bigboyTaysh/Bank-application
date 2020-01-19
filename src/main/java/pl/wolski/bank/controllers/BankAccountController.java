package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.CreditApplication;
import pl.wolski.bank.models.Currency;
import pl.wolski.bank.services.CurrencyService;

import java.util.List;

@Controller
@SessionAttributes(names = {"user"})
@Log4j2
public class BankAccountController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/newUserBankAccount")
    public String newUserBankAccount(Model model) {
        model.addAttribute("newBankAccount", new BankAccount());
        model.addAttribute("message", "");

        return "newUserBankAccountForm";
    }

    @ModelAttribute("currencyList")
    public List<Currency> loadTypes(){
        List<Currency> currencyList = currencyService.findAll();
        log.info("Ładowanie listy "+ currencyList.size() +" walut");
        return currencyList;
    }
}
