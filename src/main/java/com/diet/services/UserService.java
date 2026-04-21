package com.diet.services;

import com.diet.diettracker.dao.UserDAO;
import com.diet.diettracker.model.User;
import com.diet.diettracker.util.PasswordUtil;

public class UserService {

    private final UserDAO userDAO = new UserDAO();


    public SignupResult signup(String name, String email, String rawPassword) {
        if (name == null || name.trim().isEmpty())
            return new SignupResult(false, "Name is required.");
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            return new SignupResult(false, "Invalid email address.");
        if (rawPassword == null || rawPassword.length() < 6)
            return new SignupResult(false, "Password must be at least 6 characters.");

        if (userDAO.emailExists(email))
            return new SignupResult(false, "Email already registered.");

        String hash = PasswordUtil.hash(rawPassword);
        User user   = new User(name.trim(), email.toLowerCase(), hash);
        String id   = userDAO.save(user);
        user.setId(id);
        return new SignupResult(true, "Account created.", user);
    }


    public LoginResult login(String email, String rawPassword) {
        if (email == null || rawPassword == null)
            return new LoginResult(false, "Email and password required.", null);

        User user = userDAO.findByEmail(email.toLowerCase());
        if (user == null)
            return new LoginResult(false, "No account found with that email.", null);

        if (!PasswordUtil.verify(rawPassword, user.getPasswordHash()))
            return new LoginResult(false, "Incorrect password.", null);

        return new LoginResult(true, "Login successful.", user);
    }


    public record SignupResult(boolean success, String message, User user) {
        SignupResult(boolean success, String message) { this(success, message, null); }
    }

    public record LoginResult(boolean success, String message, User user) {}
}