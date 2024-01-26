package com.test.task.account.service.validator;

import com.test.task.account.data.entity.User;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidator {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    //Requires at least 8 characters
    //Contains at least one uppercase letter
    //Contains at least one lowercase letter
    //Contains at least one digit
    //Contains at least one special character (e.g., !@#$%^&*)
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);


    public void validate(User user) {
        if (user.getEmail() == null || !validateEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is invalid");
        }
        if (user.getPassword() == null || !validatePassword(user.getPassword())) {
            throw new IllegalArgumentException("Password is invalid");
        }
    }

    public boolean validatePassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    public boolean validateEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }
}
