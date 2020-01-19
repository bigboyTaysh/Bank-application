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
import pl.wolski.bank.services.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@SessionAttributes(names = {"user", "bankAccounts"})
@Log4j2
public class TransactionController {
    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private AddressService addressService;

    @Autowired(required = false)
    private BankAccountService bankAccountService;

    @Autowired(required = false)
    private AccountTypeService accountTypeService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/transaction")
    public String transactionForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        User user = userService.findByUsername(((UserDetails)principal).getUsername());

        model.addAttribute("user", user);
        model.addAttribute("transaction", new Transaction());

        return "transactionForm";
    }

    @PostMapping("/transaction")
    public String transaction(Model model,
                              @Valid @ModelAttribute("bankAccount") BankAccount bankAccount,
                               @Valid @ModelAttribute("transaction") Transaction transaction,
                               BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "transactionForm";
        }

        User user = (User)model.getAttribute("user");

        if((bankAccountService.findByBankAccountNumber(transaction.getToBankAccountNumber())).getCurrency().getName()
                        .equals(transaction.getCurrency().getName())){
            if(transactionService.save(user, transaction)){
                model.addAttribute("message", "Pomyślnie wykonanano przelew");
            } else {
                model.addAttribute("message", "Brak środków na koncie");
            }
        } else {
            model.addAttribute("message", "Podaj poprawną walutę");
        }

        /*
        userService.save(userForm, addressService.findExistAddress(userAddress), bankAccountService.newBankAccount(bankAccount));

         */

        return "actionMessage";
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

    @ModelAttribute("bankAccounts")
    public List<BankAccount> loadBankAccounts(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        User user = userService.findByUsername(((UserDetails)principal).getUsername());
        List<BankAccount> bankAccounts = bankAccountService.findUserAccounts(user);
        log.info("Ładowanie listy " + bankAccounts.size() + " kont bankowych ");
        return bankAccounts;
    }

    @ModelAttribute("currencyList")
    public List<Currency> loadCurrency(){
        List<Currency> currencyList = currencyService.findAll();
        log.info("Ładowanie listy " + currencyList.size() + " walut");
        return currencyList;
    }
}
