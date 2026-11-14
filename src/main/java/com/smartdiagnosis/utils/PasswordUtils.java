package com.smartdiagnosis.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * PasswordUtils - Secure password hashing using SHA-256 with salt.
 * Passwords are NEVER stored in plain text.
 * Demonstrates: Security best practices, exception handling
 */
public class PasswordUtils {

    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Hash a password with a random salt.
     * Returns "salt:hash" encoded in Base64.
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);

            return saltBase64 + ":" + hashBase64;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: algorithm not found.", e);
        }
    }

    /**
     * Verify a plain-text password against a stored hash.
     * Uses constant-time comparison to prevent timing attacks.
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null || !storedHash.contains(":")) return false;
        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 2) return false;

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHashBytes = Base64.getDecoder().decode(parts[1]);

            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] computedHash = md.digest(password.getBytes());

            if (computedHash.length != storedHashBytes.length) return false;

            // Constant-time comparison to prevent timing attacks
            int diff = 0;
            for (int i = 0; i < computedHash.length; i++) {
                diff |= computedHash[i] ^ storedHashBytes[i];
            }
            return diff == 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3
            && username.matches("[a-zA-Z0-9_]+");
    }
}
