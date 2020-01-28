package pl.wolski.bank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such transaction")
public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException(){
        super(String.format("Transakcja nie istnieje"));
    }

    public TransactionNotFoundException(Long id){
        super(String.format("Transakcja o id %d nie istnieje", id));
    }
}
