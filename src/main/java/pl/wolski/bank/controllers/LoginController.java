package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.wolski.bank.models.Role;
import pl.wolski.bank.services.UserService;

import java.util.List;
import java.util.Set;

@Controller
@Log4j2
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "/")
    //  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return "index";
        }
        return "loginForm";
    }
}



