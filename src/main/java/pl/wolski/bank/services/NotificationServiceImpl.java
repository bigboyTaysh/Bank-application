package pl.wolski.bank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wolski.bank.exceptions.NotificationNotFoundException;
import pl.wolski.bank.exceptions.UserNotFoundException;
import pl.wolski.bank.models.CreditType;
import pl.wolski.bank.models.Notification;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.CreditTypeRepository;
import pl.wolski.bank.repositories.NotificationRepository;

import java.util.List;
import java.util.Optional;


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

    @Override
    public void delete(Notification notification){
        notificationRepository.delete(notification);
    }

    @Override
    public Notification getById(Long id){
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        Notification notification =  optionalNotification.orElseThrow(() -> new NotificationNotFoundException(id));
        return notification;
    }
}