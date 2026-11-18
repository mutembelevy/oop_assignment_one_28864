
package com.auca.abstracts;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;

public abstract class Person implements Identifiable, Registrable {

    // 1. Attributes: information shared by every person
    private final String personId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    // 2. Constructor: initializes the Person information
    public Person(String personId, String firstName, String lastName,
                  String email, String phoneNumber, LocalDate dateOfBirth) {

        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    // 3. Getters: allow other classes to retrieve private attributes
    public String getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    // 4. Setters: allow controlled modification of attributes
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // 5. Identifiable: shared implementations
    @Override
    public String getId() {
        return personId;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    // 6. Registrable: written ONCE here; children extend it with super.validate()
    @Override
    public void validate() throws ValidationException {

        // Person ID: required, 4-30 characters
        if (personId == null || personId.isBlank()) {
            throw new ValidationException("Person ID is required.");
        }
        int idLength = personId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Person ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // First name: required, 2-60 characters after trimming
        if (firstName == null || firstName.isBlank()) {
            throw new ValidationException("First name is required.");
        }
        int firstLength = firstName.trim().length();
        if (firstLength < MIN_NAME_LENGTH || firstLength > MAX_NAME_LENGTH) {
            throw new ValidationException("First name must be "
                    + MIN_NAME_LENGTH + "-" + MAX_NAME_LENGTH + " characters.");
        }

        // Last name: required, 2-60 characters after trimming
        if (lastName == null || lastName.isBlank()) {
            throw new ValidationException("Last name is required.");
        }
        int lastLength = lastName.trim().length();
        if (lastLength < MIN_NAME_LENGTH || lastLength > MAX_NAME_LENGTH) {
            throw new ValidationException("Last name must be "
                    + MIN_NAME_LENGTH + "-" + MAX_NAME_LENGTH + " characters.");
        }

        // Email: required, at most 100 characters, valid syntax
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required.");
        }
        if (email.trim().length() > MAX_EMAIL_LENGTH) {
            throw new ValidationException("Email must not exceed "
                    + MAX_EMAIL_LENGTH + " characters.");
        }
        if (!email.trim().matches(
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}")) {
            throw new ValidationException(
                    "Email is not valid (example: name@example.com).");
        }

        // Phone: required, optional leading +, then 10-15 digits only
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new ValidationException("Phone number is required.");
        }
        String phone = phoneNumber.trim();
        String digits = phone.startsWith("+") ? phone.substring(1) : phone;
        if (!digits.matches("\\d+")
                || digits.length() < MIN_PHONE_LENGTH
                || digits.length() > MAX_PHONE_LENGTH) {
            throw new ValidationException("Phone number must contain "
                    + MIN_PHONE_LENGTH + "-" + MAX_PHONE_LENGTH
                    + " digits, with an optional leading +.");
        }

        // Date of birth: required and in the past
        if (dateOfBirth == null) {
            throw new ValidationException("Date of birth is required.");
        }
        if (!dateOfBirth.isBefore(LocalDate.now())) {
            throw new ValidationException("Date of birth must be in the past.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("PERSON");
        System.out.println("  01 Person ID     : " + personId);
        System.out.println("  02 First name    : " + firstName);
        System.out.println("  03 Last name     : " + lastName);
        System.out.println("  04 Email         : " + email);
        System.out.println("  05 Phone         : " + phoneNumber);
        System.out.println("  06 Date of birth : " + dateOfBirth);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + personId + "] " + getDisplayName()
                + " (" + getRoleDescription() + ")";
    }

    // 7. Methods that subclasses must still implement
    @Override
    public abstract boolean isActive();

    public abstract String getRoleDescription();

    // 8. Readable text for the object
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{personId='" + personId
                + "', name='" + getDisplayName() + "', email='" + email
                + "', phone='" + phoneNumber + "', dateOfBirth=" + dateOfBirth + "}";
    }
}

