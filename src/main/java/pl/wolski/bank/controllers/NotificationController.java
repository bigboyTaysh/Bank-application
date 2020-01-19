package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.wolski.bank.models.Role;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.services.BankAccountService;
import pl.wolski.bank.services.TransactionService;
import pl.wolski.bank.services.UserService;

import java.text.DecimalFormat;
import java.util.List;

@Log4j2
@Controller
@SessionAttributes(names = {"user", "userAccount"})
public class NotificationController {

    @Autowired
    UserService userService;

    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    TransactionService transactionService;

    @GetMapping(path = "/notifications")
    //@RequestMapping(path = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String notifications(Model model) {
        //model.addAttribute("notifications", )

        return "notifications";
    }


    /*
    @ModelAttribute("transactions")
    public List<Transaction> loadTransactions(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        log.info("Ładowanie listy " + transactions.size() + " transakcji ");
        return transactions;
    }
    */

    @InitBinder
    public void initBinder(WebDataBinder binder) {//Rejestrujemy edytory właściwości
        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        CustomNumberEditor priceEditor = new CustomNumberEditor(Float.class, numberFormat, true);
    }

}
