package pl.wolski.bank.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(String to, String title, String contents, String user) {

        MimeMessage mail = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setReplyTo(to);
            helper.setFrom("wolskiworldwidebank@gmail.com");
            helper.setSubject(title);
            helper.setText("<html>" +
                    " <body>" +
                    "<h1>Witaj " + user + "!</h1> " +
                    "<span>To jest twój link aktywacyjny: </span> " +
                    "<a href='http://127.0.0.1:8080/confirm?id=" + contents + "' >Kliknij tutaj</a>" +
                    "</body>" +
                    "</html>",true);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
        }
        javaMailSender.send(mail);
    }
}