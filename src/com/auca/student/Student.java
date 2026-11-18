
package com.auca.student;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;

import java.time.LocalDate;

public class Student extends AcademicMember {

    // Six attributes belonging specifically to Student
    private String studentNumber;
    private String programName;
    private LocalDate admissionDate;
    private int studyYear;
    private String guardianPhone;
    private boolean scholarshipStatus;

    // Constructor
    public Student(
            String personId,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth,
            String memberNumber,
            Department department,
            LocalDate joinDate,
            String academicStatus,
            String institutionalEmail,
            String advisorName,
            String studentNumber,
            String programName,
            LocalDate admissionDate,
            int studyYear,
            String guardianPhone,
            boolean scholarshipStatus) {

        // Initialize Person and AcademicMember attributes
        super(
                personId,
                firstName,
                lastName,
                email,
                phoneNumber,
                dateOfBirth,
                memberNumber,
                department,
                joinDate,
                academicStatus,
                institutionalEmail,
                advisorName
        );

        // Initialize Student-specific attributes
        this.studentNumber = studentNumber;
        this.programName = programName;
        this.admissionDate = admissionDate;
        this.studyYear = studyYear;
        this.guardianPhone = guardianPhone;
        this.scholarshipStatus = scholarshipStatus;
    }

    // Getters
    public String getStudentNumber() {
        return studentNumber;
    }

    public String getProgramName() {
        return programName;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public boolean isScholarshipStatus() {
        return scholarshipStatus;
    }

    // Setters
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public void setStudyYear(int studyYear) {
        this.studyYear = studyYear;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public void setScholarshipStatus(boolean scholarshipStatus) {
        this.scholarshipStatus = scholarshipStatus;
    }


    // Validate student information
    @Override
    public void validate() throws ValidationException {
        super.validate();   // Person + AcademicMember rules (the 12 inherited fields)

        // Student number: required, 4-30 characters
        if (studentNumber == null || studentNumber.isBlank()) {
            throw new ValidationException("Student number is required.");
        }
        int numberLength = studentNumber.trim().length();
        if (numberLength < MIN_ID_LENGTH || numberLength > MAX_ID_LENGTH) {
            throw new ValidationException("Student number must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Program name: required, 2-120 characters
        if (programName == null || programName.isBlank()) {
            throw new ValidationException("Program name is required.");
        }
        int programLength = programName.trim().length();
        if (programLength < 2 || programLength > 120) {
            throw new ValidationException("Program name must be 2-120 characters.");
        }

        // Admission date: required and not in the future
        if (admissionDate == null) {
            throw new ValidationException("Admission date is required.");
        }
        if (admissionDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Admission date must not be in the future.");
        }

        // Study year: 1 to 8
        if (studyYear < 1 || studyYear > 8) {
            throw new ValidationException("Study year must be between 1 and 8.");
        }

        // Guardian phone: required, optional leading +, then 10-15 digits only
        if (guardianPhone == null || guardianPhone.isBlank()) {
            throw new ValidationException("Guardian phone is required.");
        }
        String phone = guardianPhone.trim();
        String digits = phone.startsWith("+") ? phone.substring(1) : phone;
        if (!digits.matches("\\d+")
                || digits.length() < MIN_PHONE_LENGTH
                || digits.length() > MAX_PHONE_LENGTH) {
            throw new ValidationException("Guardian phone must contain "
                    + MIN_PHONE_LENGTH + "-" + MAX_PHONE_LENGTH
                    + " digits, with an optional leading +.");
        }
    }

    // Display student information (all 18 fields)
    @Override
    public void printDetails() {
        super.printDetails();   // PERSON (01-06) + ACADEMIC MEMBER (07-12)
        System.out.println("STUDENT");
        System.out.println("  13 Student number : " + studentNumber);
        System.out.println("  14 Program name   : " + programName);
        System.out.println("  15 Admission date : " + admissionDate);
        System.out.println("  16 Study year     : " + studyYear);
        System.out.println("  17 Guardian phone : " + guardianPhone);
        System.out.println("  18 Scholarship    : " + (scholarshipStatus ? "YES" : "NO"));
    }

    // Return a short registration summary
    @Override
    public String getRegistrationSummary() {
        return "Student " + studentNumber + " - " + getDisplayName();
    }

    // Implement Person's abstract method
    @Override
    public String getRoleDescription() {
        return "Student";
    }

    // Implement AcademicMember's abstract method
    @Override
    public String getAcademicCategory() {
        return "STUDENT";
    }

    // Readable text for the object
    @Override
    public String toString() {
        return super.toString() + " [studentNumber='" + studentNumber
                + "', program='" + programName + "', admissionDate=" + admissionDate
                + ", studyYear=" + studyYear + ", guardianPhone='" + guardianPhone
                + "', scholarship=" + scholarshipStatus + "]";
    }
}
