package com.bierocratie.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 12/05/14
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class EmailSender {

    private static final String FROM = "bierocratie@gmail.com";
    private static final String SMTP_SERVER = "smtp.free.fr";

    private static final String MAIL_ACCOUNT_CREATION_SUBJECT = "Création de compte";
    private static final String MAIL_ACCOUNT_CREATION_BODY = "Identifiant : %s\nMot de passe : %s";

    public static void sendEmailWithCredentials(String to, String... parameters) throws MessagingException {
        sendEmail(to, EmailSender.MAIL_ACCOUNT_CREATION_SUBJECT, EmailSender.MAIL_ACCOUNT_CREATION_BODY, parameters);
    }

    private static void sendEmail(String to, String subject, String body, String... parameters) throws MessagingException {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", SMTP_SERVER);

        MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties));
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(String.format(body, parameters));

        Transport.send(message);
    }

}
