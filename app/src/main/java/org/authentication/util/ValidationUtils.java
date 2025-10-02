package org.authentication.util;

import org.authentication.model.UserInfoDto;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]{3,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{6,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$"); // optional '+' and 7-15 digits

    public static void validateUser(UserInfoDto userInfo) {
        if (Objects.isNull(userInfo)) {
            throw new IllegalArgumentException("User info cannot be null");
        }

        if (Objects.isNull(userInfo.getUsername()) || !USERNAME_PATTERN.matcher(userInfo.getUsername()).matches()) {
            throw new IllegalArgumentException("Invalid username. Must be at least 3 letters/numbers.");
        }

        if (Objects.isNull(userInfo.getPassword()) || !PASSWORD_PATTERN.matcher(userInfo.getPassword()).matches()) {
            throw new IllegalArgumentException("Invalid password. Must be at least 6 characters with letters and numbers.");
        }

        if (Objects.isNull(userInfo.getEmail()) || !EMAIL_PATTERN.matcher(userInfo.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email address.");
        }

        if (Objects.nonNull(userInfo.getPhoneNo()) && !PHONE_PATTERN.matcher(userInfo.getPhoneNo()).matches()) {
            throw new IllegalArgumentException("Invalid phone number.");
        }
    }
}
