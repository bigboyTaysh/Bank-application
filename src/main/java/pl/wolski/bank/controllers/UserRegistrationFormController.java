package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.User;
import pl.wolski.bank.services.AccountTypeService;
import pl.wolski.bank.services.AddressService;
import pl.wolski.bank.services.BankAccountService;
import pl.wolski.bank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Controller
@SessionAttributes(names={"userAccount","accountTypes"})
@Log4j2
public class UserRegistrationFormController {
    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private AddressService addressService;

    @Autowired(required = false)
    private BankAccountService bankAccountService;

    @Autowired(required = false)
    private AccountTypeService accountTypeService;


    @GetMapping("/registrationForm.html")
    public String registration(Model model) {
        model.addAttribute("userAddress", new Address());
        model.addAttribute("userCommand", new User());
        model.addAttribute("message", "");

        return "registrationForm";
    }

    @PostMapping("/registrationForm.html")
    public String registration(Model model,
                               @Valid @ModelAttribute("userCommand") User userForm,
                               BindingResult bindingResult,
                               @Valid @ModelAttribute("userAddress") Address userAddress,
                               BindingResult bindingResult2) {

        if(!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("message", "Hasła muszą być takie same");
            return "registrationForm";

        }
        if (bindingResult.hasErrors()) {
            return "registrationForm";
        } else if (bindingResult2.hasErrors()){
            return "registrationForm";
        }

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

        Calendar a = Calendar.getInstance(), b = Calendar.getInstance();
        a.setTime(userForm.getBirthDate());
        b.setTime(date);

        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) &&
                        a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }

        diff = diff;
        log.info("Wiek: " + diff);


        BankAccount bankAccount = new BankAccount();

        if(diff >= 16 && diff <=26){
            bankAccount.setAccountType(
                    accountTypeService.findAccountTypeByType(AccountType.Types.PAY_ACC_FOR_YOUNG));
            userService.save(userForm, addressService.findExistAddress(userAddress));
            bankAccountService.newBankAccount(userForm, bankAccount);

        } else if (diff >= 26 && diff <= 150 ) {
            bankAccount.setAccountType(
                    accountTypeService.findAccountTypeByType(AccountType.Types.PAY_ACC_FOR_ADULT));
            userService.save(userForm, addressService.findExistAddress(userAddress));
            bankAccountService.newBankAccount(userForm, bankAccount);

        } else {
            model.addAttribute("message", "Nieprawidłowa data, bądź jesteś zbyt młody żeby samodzielnie posaidać konto w banku");
            return "registrationForm";
        }


        model.addAttribute("message", "Zostałeś pomyślnie zarejestrowany");
        return "actionMessage";
    }

    @GetMapping("/userRegistrationForm")
    public String userRegistration(Model model) {
        model.addAttribute("userAddress", new Address());
        model.addAttribute("userCommand", new User());
        model.addAttribute("message", "");

        return "userRegistrationForm";
    }

    @Secured("ROLE_EMPLOYEE")
    @PostMapping("/userRegistrationForm")
    public String userRegistration(Model model,
                               @Valid @ModelAttribute("userCommand") User userForm,
                                   BindingResult bindingResult,
                               @Valid @ModelAttribute("userAddress") Address userAddress,
                               BindingResult bindingResult2) {

        if (bindingResult.hasErrors()) {
            return "userRegistrationForm";
        } else if (bindingResult2.hasErrors()){
            return "userRegistrationForm";
        }

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

        Calendar a = Calendar.getInstance(), b = Calendar.getInstance();
        a.setTime(userForm.getBirthDate());
        b.setTime(date);

        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) &&
                        a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }

        diff = diff;
        log.info("Wiek: " + diff);


        BankAccount bankAccount = new BankAccount();

        if(diff >= 16 && diff <=26){
            bankAccount.setAccountType(
                    accountTypeService.findAccountTypeByType(AccountType.Types.PAY_ACC_FOR_YOUNG));
            userService.saveFromEmployee(userForm, addressService.findExistAddress(userAddress));
            bankAccountService.newBankAccount(userForm, bankAccount);

        } else if (diff >= 26 && diff <= 150 ) {
            bankAccount.setAccountType(
                    accountTypeService.findAccountTypeByType(AccountType.Types.PAY_ACC_FOR_ADULT));
            userService.saveFromEmployee(userForm, addressService.findExistAddress(userAddress));
            bankAccountService.newBankAccount(userForm, bankAccount);

        } else {
            model.addAttribute("message", "Nieprawidłowa data");
            return "registrationForm";
        }


        model.addAttribute("message", "Pomyślnie zarejestrowano");
        return "actionMessage";
    }


    @ModelAttribute("accountTypes")
    public List<AccountType> loadTypes(){
        List<AccountType> types = accountTypeService.getAllTypes();
        log.info("Ładowanie listy "+types.size()+" typów ");
        return types;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //aby użytkownik nie mógł sobie wstrzyknąć aktywacji konta oraz ról (np., ADMIN)
        //roles są na wszelki wypadek, bo warstwa serwisów i tak ustawia ROLE_USER dla nowego usera
        binder.setDisallowedFields("enabled", "roles");
    }


}