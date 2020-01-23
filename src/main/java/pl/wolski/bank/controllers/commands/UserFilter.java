package pl.wolski.bank.controllers.commands;

import org.springframework.util.StringUtils;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class UserFilter {

    private String phrase;

    private String personalIdentificationNumber;


    public boolean isEmpty(){
        return StringUtils.isEmpty(phrase)  && StringUtils.isEmpty(personalIdentificationNumber);
    }

    public void clear(){
        this.phrase = "";
        this.personalIdentificationNumber = "";
    }

    public String getPhraseLIKE(){
        if(StringUtils.isEmpty(phrase)) {
            return null;
        }else{
            return "%"+ phrase +"%";
        }
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPersonalIdentificationNumber(String personalIdentificationNumber) {
        this.personalIdentificationNumber = personalIdentificationNumber;
    }

    public String getPersonalIdentificationNumber() {
        return personalIdentificationNumber;
    }
}
