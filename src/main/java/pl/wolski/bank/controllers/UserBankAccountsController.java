package pl.wolski.bank.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;
import pl.wolski.bank.services.BankAccountService;
import pl.wolski.bank.services.TransactionService;
import pl.wolski.bank.services.UserService;

import java.text.DecimalFormat;
import java.util.List;


@Controller
@SessionAttributes(names = {"user", "userAccount"})
public class UserBankAccountsController {

    protected final Log log = LogFactory.getLog(getClass());//Dodatkowy komponent do logowania

    @Autowired
    UserService userService;

    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    TransactionService transactionService;

    @GetMapping(path = "/bankAccounts")
    //  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String bankAccounts(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            Object principal = auth.getPrincipal();
            User user = userService.findByUsername(((UserDetails)principal).getUsername());
            List<BankAccount> bankAccounts = bankAccountService.findUserAccounts(user);
            log.info("Ładowanie listy " + bankAccounts.size() + " kont bankowych ");
            model.addAttribute("userAccounts", bankAccounts);
            return "bankAccounts";
        }
        return "loginForm";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {//Rejestrujemy edytory właściwości
        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        CustomNumberEditor priceEditor = new CustomNumberEditor(Float.class, numberFormat, true);
    }

}