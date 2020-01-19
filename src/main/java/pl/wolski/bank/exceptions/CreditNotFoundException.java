package pl.wolski.bank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such credit")
public class CreditNotFoundException extends RuntimeException{

    public CreditNotFoundException(){
        super(String.format("Kredyt nie istnieje"));
    }

    public CreditNotFoundException(Long id){
        super(String.format("Kredyt o id %d nie istnieje", id));
    }
}
