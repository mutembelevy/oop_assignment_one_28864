
package com.auca.teacher;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Teacher extends AcademicMember {

    // Six Teacher-specific attributes
    private String employeeNumber;
    private String specialization;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String officeNumber;
    private String employmentType;

    // Constructor
    public Teacher(
            String personId, String firstName, String lastName,
            String email, String phoneNumber, LocalDate dateOfBirth,
            String memberNumber, Department department, LocalDate joinDate,
            String academicStatus, String institutionalEmail, String advisorName,
            String employeeNumber, String specialization, LocalDate hireDate,
            BigDecimal salary, String officeNumber, String employmentType) {

        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth,
                memberNumber, department, joinDate, academicStatus,
                institutionalEmail, advisorName);

        this.employeeNumber = employeeNumber;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.salary = salary;
        this.officeNumber = officeNumber;
        this.employmentType = employmentType;
    }

    // Getters
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    // Setters
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }


    @Override
    public String getAcademicCategory() {
        return "TEACHER";
    }

    // Validates the teacher's information
    @Override
    public void validate() throws ValidationException {
        super.validate();   // Person + AcademicMember rules (the 12 inherited fields)

        // Employee number: required, 4-30 characters
        if (employeeNumber == null || employeeNumber.isBlank()) {
            throw new ValidationException("Employee number is required.");
        }
        int numberLength = employeeNumber.trim().length();
        if (numberLength < MIN_ID_LENGTH || numberLength > MAX_ID_LENGTH) {
            throw new ValidationException("Employee number must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Specialization: required, 2-120 characters
        if (specialization == null || specialization.isBlank()) {
            throw new ValidationException("Specialization is required.");
        }
        int specLength = specialization.trim().length();
        if (specLength < 2 || specLength > 120) {
            throw new ValidationException(
                    "Specialization must be 2-120 characters.");
        }

        // Hire date: required and not in the future
        if (hireDate == null) {
            throw new ValidationException("Hire date is required.");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Hire date must not be in the future.");
        }

        // Salary: required and strictly positive
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Salary must be greater than zero.");
        }

        // Office number: required, at most 30 characters
        if (officeNumber == null || officeNumber.isBlank()) {
            throw new ValidationException("Office number is required.");
        }
        if (officeNumber.trim().length() > 30) {
            throw new ValidationException(
                    "Office number must not exceed 30 characters.");
        }

        // Employment type: only FULL-TIME or PART-TIME
        if (employmentType == null || employmentType.isBlank()) {
            throw new ValidationException("Employment type is required.");
        }
        if (!employmentType.equals("FULL-TIME")
                && !employmentType.equals("PART-TIME")) {
            throw new ValidationException(
                    "Employment type must be FULL-TIME or PART-TIME.");
        }
    }

    // Displays teacher information (all 18 fields)
    @Override
    public void printDetails() {
        super.printDetails();   // PERSON (01-06) + ACADEMIC MEMBER (07-12)
        System.out.println("TEACHER");
        System.out.println("  13 Employee number : " + employeeNumber);
        System.out.println("  14 Specialization  : " + specialization);
        System.out.println("  15 Hire date       : " + hireDate);
        System.out.println("  16 Salary          : "
                + (salary == null ? "null" : salary.toPlainString()));
        System.out.println("  17 Office number   : " + officeNumber);
        System.out.println("  18 Employment type : " + employmentType);
    }

    // Returns a short registration summary
    @Override
    public String getRegistrationSummary() {
        return "Teacher " + employeeNumber + " - " + getDisplayName();
    }

    // Identifies the person's role
    @Override
    public String getRoleDescription() {
        return "Teacher";
    }

    @Override
    public String toString() {
        return super.toString() + " [employeeNumber='" + employeeNumber
                + "', specialization='" + specialization + "', hireDate="
                + hireDate + ", salary="
                + (salary == null ? "null" : salary.toPlainString())
                + ", officeNumber='" + officeNumber + "', employmentType='"
                + employmentType + "']";
    }

}
