package com.bierocratie.security;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 12/05/14
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class PasswordGenerator {

    public static String generatePassword() {
        return Integer.toString((int) (Math.random() * Integer.MAX_VALUE), 36);
    }

}
