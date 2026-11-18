
package com.auca.abstracts;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import java.time.LocalDate;

public abstract class AcademicMember extends Person {

    // 1. Six attributes required by the assignment
    private String memberNumber;
    private Department department;
    private LocalDate joinDate;
    private String academicStatus;
    private String institutionalEmail;
    private String advisorName;

    // 2. Constructor
    public AcademicMember(
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
            String advisorName) {

        // Initialize the six attributes inherited from Person
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);

        // Initialize AcademicMember's own six attributes
        this.memberNumber = memberNumber;
        this.department = department;
        this.joinDate = joinDate;
        this.academicStatus = academicStatus;
        this.institutionalEmail = institutionalEmail;
        this.advisorName = advisorName;
    }

    // 3. Getters
    public String getMemberNumber() {
        return memberNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public String getInstitutionalEmail() {
        return institutionalEmail;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    // 4. Setters
    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public void setInstitutionalEmail(String institutionalEmail) {
        this.institutionalEmail = institutionalEmail;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    // 5. Identifiable: written once here, shared by Student and Teacher
    @Override
    public boolean isActive() {
        return DEFAULT_STATUS.equals(academicStatus);   // "ACTIVE"
    }

    // 6. Registrable: Person's rules first (super), then our own six fields
    @Override
    public void validate() throws ValidationException {
        super.validate();

        // Member number: required, 4-30 characters
        if (memberNumber == null || memberNumber.isBlank()) {
            throw new ValidationException("Member number is required.");
        }
        int numberLength = memberNumber.trim().length();
        if (numberLength < MIN_ID_LENGTH || numberLength > MAX_ID_LENGTH) {
            throw new ValidationException("Member number must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Department: must be a real, selected Department object
        if (department == null) {
            throw new ValidationException("Department is required.");
        }

        // Join date: required and not in the future
        if (joinDate == null) {
            throw new ValidationException("Join date is required.");
        }
        if (joinDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Join date must not be in the future.");
        }

        // Academic status: only ACTIVE or INACTIVE
        if (academicStatus == null || academicStatus.isBlank()) {
            throw new ValidationException("Academic status is required.");
        }
        if (!academicStatus.equals(DEFAULT_STATUS)
                && !academicStatus.equals("INACTIVE")) {
            throw new ValidationException(
                    "Academic status must be ACTIVE or INACTIVE.");
        }

        // Institutional email: required, at most 100 characters, valid syntax
        if (institutionalEmail == null || institutionalEmail.isBlank()) {
            throw new ValidationException("Institutional email is required.");
        }
        if (institutionalEmail.trim().length() > MAX_EMAIL_LENGTH) {
            throw new ValidationException("Institutional email must not exceed "
                    + MAX_EMAIL_LENGTH + " characters.");
        }
        if (!institutionalEmail.trim().matches(
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}")) {
            throw new ValidationException(
                    "Institutional email is not valid (example: name@auca.ac.rw).");
        }

        // Advisor name: required, 2-120 characters after trimming
        if (advisorName == null || advisorName.isBlank()) {
            throw new ValidationException("Advisor name is required.");
        }
        int advisorLength = advisorName.trim().length();
        if (advisorLength < 2 || advisorLength > 120) {
            throw new ValidationException(
                    "Advisor name must be 2-120 characters.");
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("ACADEMIC MEMBER");
        System.out.println("  07 Member number      : " + memberNumber);
        System.out.println("  08 Department         : " + (department == null
                ? "Not assigned"
                : "[" + department.getId() + "] " + department.getDisplayName()));
        System.out.println("  09 Join date          : " + joinDate);
        System.out.println("  10 Academic status    : " + academicStatus);
        System.out.println("  11 Institutional email: " + institutionalEmail);
        System.out.println("  12 Advisor name       : " + advisorName);
    }

    // 7. Every AcademicMember subclass must define its category
    public abstract String getAcademicCategory();

    // 8. Readable text for the object
    @Override
    public String toString() {
        return super.toString() + " [memberNumber='" + memberNumber
                + "', department=" + (department == null
                ? "none" : department.getDisplayName())
                + ", joinDate=" + joinDate + ", status=" + academicStatus
                + ", institutionalEmail='" + institutionalEmail
                + "', advisor='" + advisorName + "']";
    }
}
