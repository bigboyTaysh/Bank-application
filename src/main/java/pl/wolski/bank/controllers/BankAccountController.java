package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.wolski.bank.models.*;
import pl.wolski.bank.repositories.CurrencyRepository;
import pl.wolski.bank.services.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@SessionAttributes(names = {"user"})
@Log4j2
public class BankAccountController {
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private AccountTypeService accountTypeService;

    @GetMapping("/newUserBankAccount")
    public String newUserBankAccount(Model model) {
        model.addAttribute("newBankAccount", new BankAccount());
        model.addAttribute("message", "");

        return "newUserBankAccountForm";
    }

    @PostMapping("/newUserBankAccount")
    public String newUserBankAccount(Model model,
                              @Valid @ModelAttribute("newBankAccount") BankAccount bankAccount,
                              BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "newUserBankAccountForm";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        User user = userService.findByUsername(((UserDetails)principal).getUsername());

        if(bankAccountService.findByUserAndCurrency(user, currencyService.findByName(bankAccount.getCurrency().getName())) != null){
            model.addAttribute("message", "Posiadasz już takie konto");

            return "newUserBankAccountForm";
        } else {
            bankAccount.setAccountType(accountTypeService.findAccountTypeByType(AccountType.Types.FOR_CUR_ACC));
            bankAccountService.newBankAccount(userService.findByUsername(((UserDetails)principal).getUsername()), bankAccount);

            model.addAttribute("message", "Pomyślnie utworzono konto!");

            return "actionMessage";
        }
    }

    @ModelAttribute("currencyList")
    public List<Currency> loadTypes(){
        List<Currency> currencyList = currencyService.findAll();
        int position = 0;
        for (int i = 0; i < currencyList.size(); i++) {
            if (currencyList.get(i).getName().equals("PLN")){
                position = i;
            }
        }
        currencyList.remove(position);

        log.info("Ładowanie listy "+ currencyList.size() +" walut");
        return currencyList;
    }

    @ModelAttribute("notificationCounter")
    public int notificationCounter(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        User user = userService.findByUsername(((UserDetails)principal).getUsername());
        List<Notification> notificationList = notificationService.findByUserAndWasRead(user, false);
        log.info("Ładowanie listy " + notificationList.size() + " kont bankowych ");
        return notificationList.size();
    }
}
