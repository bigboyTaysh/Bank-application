package pl.wolski.bank.services;

import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.Currency;
import pl.wolski.bank.models.User;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountService {
// Własne metody
    void save(BankAccount bankAccount, AccountType accountType);
    void save(BankAccount bankAccount);
    BankAccount findByBankAccountNumber(BigDecimal bankAccountNumber);
    void runThread(BigDecimal fromBankAccountNumber, BigDecimal value);
    BankAccount newBankAccount(User user, BankAccount bankAccount);
    BankAccount getUserAccount(User user);
    List<BankAccount> findUserAccounts(User user);
    BankAccount findByUserAndCurrency(User user, Currency currency);
}
