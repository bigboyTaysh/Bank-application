package pl.wolski.bank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such bank account")
public class BankAccountNotFoundException extends RuntimeException{

    public BankAccountNotFoundException(){
        super(String.format("Konto bankowe nie istnieje"));
    }

    public BankAccountNotFoundException(Long id){
        super(String.format("Konto bankowe o id %d nie istnieje", id));
    }
}
