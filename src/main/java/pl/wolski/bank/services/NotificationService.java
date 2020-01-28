package pl.wolski.bank.services;


import pl.wolski.bank.models.AccountType;
import pl.wolski.bank.models.Notification;
import pl.wolski.bank.models.User;

import java.util.List;

public interface NotificationService {
// Własne metody
    void save(Notification notification);

    List<Notification> findByUserAndWasRead(User user, boolean wasRead);

    List<Notification> getAllUserNotification(User user);

    void delete(Notification notification);

    Notification getById(Long id);
}
