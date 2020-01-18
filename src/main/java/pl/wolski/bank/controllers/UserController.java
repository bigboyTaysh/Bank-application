package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Role;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;
import pl.wolski.bank.services.BankAccountService;
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
    UserService userService;

    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    TransactionService transactionService;

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
                List<Transaction> transactions = transactionService.findUserTransactions(
                        bankAccountService.getUserAccount(userService.findByUsername(((UserDetails) principal).getUsername())).getBankAccountNumber(),
                        bankAccountService.getUserAccount(userService.findByUsername(((UserDetails) principal).getUsername())).getBankAccountNumber());
                model.addAttribute("transactions", transactions);
                model.addAttribute("userAccount", bankAccountService.getUserAccount(userService.findByUsername(((UserDetails) principal).getUsername())));

                return "index";
            }
        }
        return "loginForm";
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
