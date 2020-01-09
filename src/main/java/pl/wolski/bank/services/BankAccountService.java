package pl.wolski.bank.services;

import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.BankAccount;

public interface BankAccountService {
// Własne metody
    void save(BankAccount bankAccount, AccountType accountType);
    BankAccount newBankAccount(BankAccount bankAccount);
}
