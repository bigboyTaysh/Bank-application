package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.models.Notification;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.CreditTypeRepository;
import pl.wolski.bank.repositories.NotificationRepository;

import java.util.List;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void save(Notification notification) {
        notificationRepository.saveAndFlush(notification);
    }

    @Override
    public List<Notification> getAllUserNotification(User user){
        return notificationRepository.findByUserOrderByDateDesc(user);
    }

    @Override
    public List<Notification> findByUserAndWasRead(User user, boolean wasRead){
        return notificationRepository.findByUserAndWasRead(user, wasRead);
    }
}