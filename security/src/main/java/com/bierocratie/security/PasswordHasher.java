package com.bierocratie.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 20/05/14
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public class PasswordHasher {

    public static byte[] hashPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(password.getBytes("UTF-8"));
    }

}
