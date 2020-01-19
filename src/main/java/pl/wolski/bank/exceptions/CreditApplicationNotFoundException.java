package pl.wolski.bank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such credit application")
public class CreditApplicationNotFoundException extends RuntimeException{

    public CreditApplicationNotFoundException(){
        super(String.format("Wniosek o kredyt nie istnieje"));
    }

    public CreditApplicationNotFoundException(Long id){
        super(String.format("Wniosek o kredyt o id %d nie istnieje", id));
    }
}
