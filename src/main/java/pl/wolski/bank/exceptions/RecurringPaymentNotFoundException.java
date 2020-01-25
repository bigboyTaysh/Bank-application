package pl.wolski.bank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such recurring payment")
public class RecurringPaymentNotFoundException extends RuntimeException{

    public RecurringPaymentNotFoundException(){
        super(String.format("Płatność cykliczna nie istnieje"));
    }

    public RecurringPaymentNotFoundException(Long id){
        super(String.format("Płatność cykliczna o id %d nie istnieje", id));
    }
}
