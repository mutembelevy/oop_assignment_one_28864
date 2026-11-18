
package com.auca.department;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;
import com.auca.teacher.Teacher;

public class Department implements Identifiable, Registrable {

    // Six Department attributes
    private final String departmentId;
    private String departmentName;
    private String departmentCode;
    private String officeLocation;
    private String contactEmail;
    private Teacher headTeacher;

    // Constructor
    public Department(
            String departmentId,
            String departmentName,
            String departmentCode,
            String officeLocation,
            String contactEmail,
            Teacher headTeacher) {

        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.officeLocation = officeLocation;
        this.contactEmail = contactEmail;
        this.headTeacher = headTeacher;
    }

    // Getters
    public String getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Teacher getHeadTeacher() {
        return headTeacher;
    }

    // Setters
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setHeadTeacher(Teacher headTeacher) {
        this.headTeacher = headTeacher;
    }

    // Identifiable interface methods
    @Override
    public String getId() {
        return departmentId;
    }

    @Override
    public String getDisplayName() {
        return departmentName;
    }

    @Override
    public boolean isActive() {
        return departmentId != null && !departmentId.isBlank()
                && departmentName != null && !departmentName.isBlank();
    }

    // Registrable interface methods
    @Override
    public void validate() throws ValidationException {

        // Department ID: required, 4-30 characters
        if (departmentId == null || departmentId.isBlank()) {
            throw new ValidationException("Department ID is required.");
        }
        int idLength = departmentId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Department ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Department name: required, 2-60 characters after trimming
        if (departmentName == null || departmentName.isBlank()) {
            throw new ValidationException("Department name is required.");
        }
        int nameLength = departmentName.trim().length();
        if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
            throw new ValidationException("Department name must be "
                    + MIN_NAME_LENGTH + "-" + MAX_NAME_LENGTH + " characters.");
        }

        // Department code: required, 2-20 characters
        if (departmentCode == null || departmentCode.isBlank()) {
            throw new ValidationException("Department code is required.");
        }
        int codeLength = departmentCode.trim().length();
        if (codeLength < 2 || codeLength > 20) {
            throw new ValidationException(
                    "Department code must be 2-20 characters.");
        }

        // Office location: required, at most 60 characters
        if (officeLocation == null || officeLocation.isBlank()) {
            throw new ValidationException("Office location is required.");
        }
        if (officeLocation.trim().length() > 60) {
            throw new ValidationException(
                    "Office location must not exceed 60 characters.");
        }

        // Contact email: required, at most 100 characters, valid syntax
        if (contactEmail == null || contactEmail.isBlank()) {
            throw new ValidationException("Contact email is required.");
        }
        if (contactEmail.trim().length() > MAX_EMAIL_LENGTH) {
            throw new ValidationException("Contact email must not exceed "
                    + MAX_EMAIL_LENGTH + " characters.");
        }
        if (!contactEmail.trim().matches(
                "[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}")) {
            throw new ValidationException(
                    "Contact email is not valid (example: cs@auca.ac.rw).");
        }

        // Head teacher: optional at creation (null is allowed)
    }

    @Override
    public void printDetails() {
        System.out.println("DEPARTMENT");
        System.out.println("  01 Department ID   : " + departmentId);
        System.out.println("  02 Department name : " + departmentName);
        System.out.println("  03 Department code : " + departmentCode);
        System.out.println("  04 Office location : " + officeLocation);
        System.out.println("  05 Contact email   : " + contactEmail);
        System.out.println("  06 Head teacher    : " + (headTeacher == null
                ? "Not assigned"
                : headTeacher.getDisplayName()));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + departmentId + "] " + departmentName
                + " (" + departmentCode + ")";
    }

    // Readable text for the object
    @Override
    public String toString() {
        return "Department{departmentId='" + departmentId
                + "', name='" + departmentName + "', code='" + departmentCode
                + "', office='" + officeLocation + "', email='" + contactEmail
                + "', headTeacher=" + (headTeacher == null
                ? "none" : headTeacher.getDisplayName()) + "}";
    }
}
