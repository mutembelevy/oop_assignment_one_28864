package com.auca.interfaces;

import com.auca.exceptions.ValidationException;

public interface Registrable {

    // Shared validation constants (public static final by default)
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 60;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MIN_PHONE_LENGTH = 10;
    public static final int MAX_PHONE_LENGTH = 15;
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    // Contract: every implementing class must provide these methods
    void validate() throws ValidationException;

    void printDetails();

    String getRegistrationSummary();
}