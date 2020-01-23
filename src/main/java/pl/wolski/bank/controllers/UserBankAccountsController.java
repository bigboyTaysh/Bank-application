package pl.wolski.bank.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Notification;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;
import pl.wolski.bank.services.BankAccountService;
import pl.wolski.bank.services.NotificationService;
import pl.wolski.bank.services.TransactionService;
import pl.wolski.bank.services.UserService;

import java.math.BigDecimal;
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

    @Autowired
    private NotificationService notificationService;

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

    @PostMapping(path = "/userBankAccount")
    //@RequestMapping(path = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String userBankAccount(Model model,
                                  @RequestParam(value = "bankAccountNumber", required = false) String bankAccountNumber) {

        BigDecimal number = new BigDecimal(bankAccountNumber);
        List<Transaction> transactions = transactionService.findUserTransactions(number,number);

        model.addAttribute("transactions", transactions);
        model.addAttribute("userAccount", bankAccountService.findByBankAccountNumber(number));


        return "userBankAccount";
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {//Rejestrujemy edytory właściwości
        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        CustomNumberEditor priceEditor = new CustomNumberEditor(Float.class, numberFormat, true);
    }

}
