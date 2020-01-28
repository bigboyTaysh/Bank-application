package pl.wolski.bank.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public void newPassword(String to, String title, String contents, String user) {

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
                    "<span>Ustaw nowe hasło: </span> " +
                    "<a href='http://127.0.0.1:8080/newPassword?confirmationId=" + contents + "' >Kliknij tutaj</a>" +
                    "</body>" +
                    "</html>",true);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
        }
        javaMailSender.send(mail);
    }

    public void sendConfirmation(String to, String title, String id) {

        MimeMessage mail = javaMailSender.createMimeMessage();

        try {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            String SERVER_LOCATION = s + "\\src\\main\\resources\\static\\confirmation\\";
            File file = new File(SERVER_LOCATION + id + ".pdf");

            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setReplyTo(to);
            helper.setFrom("wolskiworldwidebank@gmail.com");
            helper.setSubject(title);
            helper.addAttachment(file.getName(), file);
            helper.setText("<html>" +
                    " <body>" +
                    "<h1>Witaj!</h1> " +
                    "<p>Witamy, " +
                    "w załączniku przesyłamy potwierdzenie operacji w serwisie bankowości elektronicznej WWB.</p>" +
                    "<p>Pozdrawiamy</p>" +
                    "</body>" +
                    "</html>",true);
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
        }
        javaMailSender.send(mail);
    }

}