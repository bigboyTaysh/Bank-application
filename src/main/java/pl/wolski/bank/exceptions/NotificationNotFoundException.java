package pl.wolski.bank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such notification")
public class NotificationNotFoundException extends RuntimeException{

    public NotificationNotFoundException(){
        super(String.format("Powiadomienie nie istnieje"));
    }

    public NotificationNotFoundException(Long id){
        super(String.format("Powiadomienie o id %d nie istnieje", id));
    }
}
