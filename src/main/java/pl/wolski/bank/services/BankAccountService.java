package pl.wolski.bank.services;

import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.BankAccount;
import pl.wolski.bank.models.User;

import java.util.List;

public interface BankAccountService {
// Własne metody
    void save(BankAccount bankAccount, AccountType accountType);
    void save(BankAccount bankAccount);
    BankAccount newBankAccount(User user, BankAccount bankAccount);
    BankAccount getUserAccount(User user);
    List<BankAccount> findUserAccounts(User user);
}
