package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.wolski.bank.models.*;
import pl.wolski.bank.services.CreditApplicationService;
import pl.wolski.bank.services.CreditService;
import pl.wolski.bank.services.CreditTypeService;
import pl.wolski.bank.services.UserService;

import javax.validation.Valid;
import java.util.List;

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

    @Autowired(required = false)
    private CreditTypeService creditTypeService;

    @GetMapping("/creditApplication")
    public String creditForm(Model model) {
        model.addAttribute("creditApplication", new CreditApplication());

        return "creditApplicationForm";
    }

    @PostMapping("/creditApplication")
    public String creditForm2(@Valid @ModelAttribute("creditApplication") CreditApplication creditApplication,
                             @RequestParam(required = true) Double creditRates,
                             BindingResult bindingResult){
        return "index";
    }

    @ModelAttribute("creditTypes")
    public List<CreditType> loadCreditTypes(){
        List<CreditType> creditTypes = creditTypeService.findAll();
        log.info("Ładowanie listy " + creditTypes.size() + " typów kredytów ");
        return creditTypes;
    }
}
