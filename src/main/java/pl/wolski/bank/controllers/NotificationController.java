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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.wolski.bank.models.*;
import pl.wolski.bank.services.BankAccountService;
import pl.wolski.bank.services.NotificationService;
import pl.wolski.bank.services.TransactionService;
import pl.wolski.bank.services.UserService;

import java.text.DecimalFormat;
import java.util.List;

@Log4j2
@Controller
@SessionAttributes(names = {"user", "userAccount"})
public class NotificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping(path = "/notifications")
    //@RequestMapping(path = "/index", method = {RequestMethod.GET, RequestMethod.POST})
    public String notifications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        model.addAttribute("notifications", notificationService.getAllUserNotification(userService.findByUsername(((UserDetails) principal).getUsername())));

        return "notifications";
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
