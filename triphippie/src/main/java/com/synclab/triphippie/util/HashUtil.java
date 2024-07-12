package com.synclab.triphippie.util;


import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class HashUtil {

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] extractSalt(String storedHash) {
        byte[] hashWithSalt = Base64.getDecoder().decode(storedHash);
        byte[] salt = new byte[16];
        System.arraycopy(hashWithSalt, 0, salt, 0, 16);
        return salt;
    }

    private static String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt);
            byte[] hash = messageDigest.digest(password.getBytes());
            byte[] hashWithSalt = new byte[hash.length + salt.length];
            System.arraycopy(salt, 0, hashWithSalt, 0, salt.length);
            System.arraycopy(hash, 0, hashWithSalt, salt.length, hash.length);
            return Base64.getEncoder().encodeToString(hashWithSalt);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static String hashPassword(String password) {
        byte[] salt = generateSalt();
        return hashPassword(password, salt);
    }

    /**
     * @param password plain password
     * @param storedHash hashed password extracted from the database
     * @return true if the hash of the password is equal to the storedHash
     */
    public static boolean verifyPassword(String password, String storedHash) {
        byte[] salt = extractSalt(storedHash);
        String hashToVerify = hashPassword(password, salt);
        return hashToVerify.equals(storedHash);
    }
}
