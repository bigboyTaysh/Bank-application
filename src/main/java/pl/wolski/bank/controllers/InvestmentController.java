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
import pl.wolski.bank.models.Investment;
import pl.wolski.bank.models.InvestmentType;
import pl.wolski.bank.services.InvestmentService;
import pl.wolski.bank.services.InvestmentTypeService;
import pl.wolski.bank.services.NotificationService;
import pl.wolski.bank.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@SessionAttributes(names = {"user"})
@Log4j2
public class InvestmentController {
    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private InvestmentService investmentService;

    @Autowired(required = false)
    private InvestmentTypeService investmentTypeService;

    @Autowired
    private NotificationService notificationService;


    @GetMapping("/investmentForm")
    public String showInvestmentForm(Model model) {
        model.addAttribute("investment", new Investment());
        return "investmentForm";
    }

    @PostMapping("/investmentForm")
    public String investmentForm(Model model,
                             @Valid @ModelAttribute("investment") Investment investment,
                             BindingResult bindingResult){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        /*if (investmentService.cashCheck(((UserDetails)principal).getUsername(), investment.getInvestmentAmount()) == false){
            model.addAttribute("message", "Brak kaski na koncie!");
            return "actionMessage";
        }*/
        investmentService.save(investment,((UserDetails)principal).getUsername());
        model.addAttribute("message", "Pomyślnie wypełniono wniosek!");

        return "actionMessage";
    }

    @ModelAttribute("investmentTypes")
    public List<InvestmentType> loadInvestmentTypes(){
        List<InvestmentType> investmentTypes = investmentTypeService.findAll();
        log.info("Ładowanie listy " + investmentTypes.size() + " typów lokat ");
        return investmentTypes;
    }

    @GetMapping("/userInvestments")
    public String userInvestments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        model.addAttribute("userInvestments", investmentService.findByUser(
                userService.findByUsername(((UserDetails)principal).getUsername())));

        return "userInvestments";
    }
}
