
package com.auca.administrator;

import com.auca.abstracts.Person;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;

public class Administrator extends Person {

    // Six Administrator-specific attributes
    private String staffNumber;
    private String jobTitle;
    private String assignedOffice;
    private LocalDate employmentDate;
    private int accessLevel;
    private boolean active;

    // Constructor
    public Administrator(
            String personId, String firstName, String lastName,
            String email, String phoneNumber, LocalDate dateOfBirth,
            String staffNumber, String jobTitle, String assignedOffice,
            LocalDate employmentDate, int accessLevel, boolean active) {

        // Initialize inherited Person attributes
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);

        // Initialize Administrator attributes
        this.staffNumber = staffNumber;
        this.jobTitle = jobTitle;
        this.assignedOffice = assignedOffice;
        this.employmentDate = employmentDate;
        this.accessLevel = accessLevel;
        this.active = active;
    }

    // Getters
    public String getStaffNumber() {
        return staffNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getAssignedOffice() {
        return assignedOffice;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    // Setters
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setAssignedOffice(String assignedOffice) {
        this.assignedOffice = assignedOffice;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Implements Person's abstract methods

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();   // Person rules (the 6 inherited fields)

        // Staff number: required, 4-30 characters
        if (staffNumber == null || staffNumber.isBlank()) {
            throw new ValidationException("Staff number is required.");
        }
        int numberLength = staffNumber.trim().length();
        if (numberLength < MIN_ID_LENGTH || numberLength > MAX_ID_LENGTH) {
            throw new ValidationException("Staff number must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Job title: required, 2-120 characters
        if (jobTitle == null || jobTitle.isBlank()) {
            throw new ValidationException("Job title is required.");
        }
        int titleLength = jobTitle.trim().length();
        if (titleLength < 2 || titleLength > 120) {
            throw new ValidationException("Job title must be 2-120 characters.");
        }

        // Assigned office: required, at most 30 characters
        if (assignedOffice == null || assignedOffice.isBlank()) {
            throw new ValidationException("Assigned office is required.");
        }
        if (assignedOffice.trim().length() > 30) {
            throw new ValidationException(
                    "Assigned office must not exceed 30 characters.");
        }

        // Employment date: required and not in the future
        if (employmentDate == null) {
            throw new ValidationException("Employment date is required.");
        }
        if (employmentDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Employment date must not be in the future.");
        }

        // Access level: 1 (lowest) to 5 (highest)
        if (accessLevel < 1 || accessLevel > 5) {
            throw new ValidationException(
                    "Access level must be between 1 and 5.");
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();   // PERSON (01-06)
        System.out.println("ADMINISTRATOR");
        System.out.println("  07 Staff number    : " + staffNumber);
        System.out.println("  08 Job title       : " + jobTitle);
        System.out.println("  09 Assigned office : " + assignedOffice);
        System.out.println("  10 Employment date : " + employmentDate);
        System.out.println("  11 Access level    : " + accessLevel);
        System.out.println("  12 Active          : " + (active ? "YES" : "NO"));
    }

    @Override
    public String getRegistrationSummary() {
        return "Administrator " + staffNumber + " - " + getDisplayName();
    }

    @Override
    public String getRoleDescription() {
        return "Administrator";
    }

    // Readable text for the object
    @Override
    public String toString() {
        return super.toString() + " [staffNumber='" + staffNumber
                + "', jobTitle='" + jobTitle + "', office='" + assignedOffice
                + "', employmentDate=" + employmentDate
                + ", accessLevel=" + accessLevel + ", active=" + active + "]";
    }
}

