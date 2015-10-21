package com.bierocratie.security;

import com.bierocratie.email.EmailSender;
import com.bierocratie.model.security.Account;
import com.bierocratie.model.security.Role;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 20/05/14
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
public class AdminGenerator {

    private static final String ADMIN_LOGIN = "bierocratie";
    private static final String ADMIN_EMAIL = "pierre.gidel@gmail.com";

    public static Account generateAdminAccount() throws MessagingException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String password = PasswordGenerator.generatePassword();

        Account admin = new Account();
        admin.setLogin(ADMIN_LOGIN);
        admin.setPassword(PasswordHasher.hashPassword(password));
        admin.setEmail(ADMIN_EMAIL);
        admin.setRole(Role.ADMIN);

        EmailSender.sendEmailWithCredentials(admin.getEmail(), admin.getLogin(), password);

        return admin;
    }

}
