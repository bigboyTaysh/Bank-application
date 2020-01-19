package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.wolski.bank.models.*;
import pl.wolski.bank.services.*;

import javax.validation.Valid;
import java.math.BigDecimal;
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

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/creditApplication")
    public String creditForm(Model model) {
        model.addAttribute("creditApplication", new CreditApplication());

        return "creditApplicationForm";
    }

    @GetMapping("/userCredits")
    public String userCredits(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        model.addAttribute("userCredits", creditService.findByUser(
                userService.findByUsername(((UserDetails)principal).getUsername())));

        return "userCredits";
    }

    //@PostMapping(path = "/userCredits", params={"value"})
    //@GetMapping("/userCredits")
    @PostMapping(value = "/userCredits")
    public String payOffTheCredit(Model model,
                                  @RequestParam(value = "id", required = false) String id,
                                  @RequestParam(value = "credit", required = false) String credit) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();


        if(creditService.payOffTheCredit(Long.parseLong(id), new BigDecimal(credit), userService.findByUsername(((UserDetails)principal).getUsername()))){
            model.addAttribute("message", "Pomyślnie zapłacono");
        } else {
            model.addAttribute("message", "Nie udało się zapłacić");
        }

        return "actionMessage";
    }


    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @GetMapping("/creditApplicationsList")
    public String creditApplicationsList(Model model) {
        model.addAttribute("creditApplicationsList", creditApplicationService.findAll());

        return "creditApplicationsList";
    }

    @PostMapping("/creditApplication")
    public String creditForm(Model model,
                              @Valid @ModelAttribute("creditApplication") CreditApplication creditApplication,
                             BindingResult bindingResult){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        creditApplicationService.save(creditApplication, ((UserDetails)principal).getUsername());

        model.addAttribute("message", "Pomyślnie wypełniono wniosek!");

        return "actionMessage";
    }

    @ModelAttribute("creditTypes")
    public List<CreditType> loadCreditTypes(){
        List<CreditType> creditTypes = creditTypeService.findAll();
        log.info("Ładowanie listy " + creditTypes.size() + " typów kredytów ");
        return creditTypes;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/acceptApplication", method = RequestMethod.GET)
    public String acceptApplication(Model model, Long id) {
        creditApplicationService.updateCreditApplicationStatus(id, true);

        return "redirect:/creditApplicationsList";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/discardApplication", method = RequestMethod.GET)
    public String discardApplication(Model model, Long id) {
        creditApplicationService.updateCreditApplicationStatus(id, false);

        return "redirect:/creditApplicationsList";
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
