package pl.wolski.bank.controllers.commands;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import pl.wolski.bank.models.User;
import pl.wolski.bank.models.User_;

import java.math.BigDecimal;

public class UserSpecifications {
    public static Specification<User> findAll(final String type)
    {
        return (root, query, cb) -> {
            if(StringUtils.isEmpty(type) == false){
                String typeLike = "%"+type.toUpperCase() +"%";
                return cb.equal(root.join("roles").get("type").as(String.class), type
                );
            }
            return null;
        };
    }

    public static Specification<User> findByPhrase(final String phrase, String type)
    {
        return (root, query, cb) -> {
            if(StringUtils.isEmpty(phrase) == false && StringUtils.isEmpty(type) == false){
                String phraseLike = "%"+phrase.toUpperCase() +"%";
                String typeLike = "%"+type.toUpperCase() +"%";
                return cb.and(
                        cb.or(
                                cb.like(cb.upper(root.get(User_.firstName)), phraseLike),
                                cb.like(cb.upper(root.get(User_.lastName)), phraseLike)
                        ),
                        cb.equal(root.join("roles").get("type").as(String.class), type)
                );
            }
            return null;
        };
    }

    public static Specification<User> findByPersonalIdentificationNumber(String personalIdentificationNumber) {
        return (root, query, cb) -> {
            if(personalIdentificationNumber != null){
                String pinLike = "%"+personalIdentificationNumber.toUpperCase() +"%";
                return cb.like(root.get("personalIdentificationNumber").as(String.class), pinLike);
            }
            return null;
        };
    }
}