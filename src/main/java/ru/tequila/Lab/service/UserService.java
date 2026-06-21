package ru.tequila.Lab.service;

import ru.tequila.Lab.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }

        String passwordHash = hashPassword(password);
        return userRepository.registerUser(username.trim(), passwordHash);
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String storedHash = userRepository.getPasswordHash(username.trim());
        if (storedHash == null) {
            return false;
        }

        return storedHash.equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("ошибка: SHA-256 не поддерживается JVM", e);
        }
    }
}
