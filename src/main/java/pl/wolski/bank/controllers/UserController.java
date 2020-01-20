package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.wolski.bank.models.*;
import pl.wolski.bank.services.BankAccountService;
import pl.wolski.bank.services.NotificationService;
import pl.wolski.bank.services.TransactionService;
import pl.wolski.bank.services.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@SessionAttributes(names = {"user", "userAccount"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping(path = "/index")
    //@RequestMapping(path = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :slight_smile: */
            List<Role> userRole = (userService.findRoleByUser(userService.findByUsername(((UserDetails) principal).getUsername())));

            boolean isAdmin = false;

            for (Role role : userRole) {
                if (role.getType() == Role.Types.ROLE_ADMIN || role.getType() == Role.Types.ROLE_EMPLOYEE)
                    isAdmin = true;
            }

            if (isAdmin == true) {
                return "redirect:/creditApplicationsList";
            } else {
                List<Transaction> transactions = transactionService.findUserTop5Transactions(
                        bankAccountService.getUserAccount(userService.findByUsername(((UserDetails) principal).getUsername())).getBankAccountNumber(),
                        bankAccountService.getUserAccount(userService.findByUsername(((UserDetails) principal).getUsername())).getBankAccountNumber());
                model.addAttribute("transactions", transactions);
                model.addAttribute("userAccount", bankAccountService.getUserAccount(userService.findByUsername(((UserDetails) principal).getUsername())));

                return "index";
            }
        }
        return "loginForm";
    }

    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @GetMapping(path = "/user")
    //@RequestMapping(path = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String user(Model model,
                       Long id) {
        User user = userService.findById(id);

        model.addAttribute("user", user);

        List<Transaction> transactions = transactionService.findUserTop5Transactions(
                bankAccountService.getUserAccount(userService.findByUsername(user.getUsername())).getBankAccountNumber(),
                bankAccountService.getUserAccount(userService.findByUsername(user.getUsername())).getBankAccountNumber());
        model.addAttribute("transactions", transactions);
        model.addAttribute("userAccounts", bankAccountService.findUserAccounts(user));


        return "user";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "/users")
    //@RequestMapping(path = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String users(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();


        return "users";
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
