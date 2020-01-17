package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.wolski.bank.models.CreditApplication;
import pl.wolski.bank.models.Transaction;
import pl.wolski.bank.models.User;
import pl.wolski.bank.services.CreditApplicationService;
import pl.wolski.bank.services.CreditService;
import pl.wolski.bank.services.UserService;

@Controller
@SessionAttributes(names = {"user"})
@Log4j2
public class CreditController {
    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private CreditApplicationService creditApplicationService;

    @Autowired(required = false)
    private CreditService creditService;

    @GetMapping("/creditApplication")
    public String transactionForm(Model model) {
        model.addAttribute("creditApplication", new CreditApplication());

        return "creditApplicationForm";
    }
}
