
package com.auca.course;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;
import com.auca.department.Department;
import com.auca.teacher.Teacher;

public class Course implements Identifiable, Registrable {

    // Six Course attributes
    private  final String courseId;
    private String courseCode;
    private String courseTitle;
    private int creditHours;
    private Department department;
    private Teacher assignedTeacher;

    // Constructor
    public Course(
            String courseId,
            String courseCode,
            String courseTitle,
            int creditHours,
            Department department,
            Teacher assignedTeacher) {

        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
        this.department = department;
        this.assignedTeacher = assignedTeacher;
    }

    // Getters
    public String getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public Department getDepartment() {
        return department;
    }

    public Teacher getAssignedTeacher() {
        return assignedTeacher;
    }

    // Setters
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setAssignedTeacher(Teacher assignedTeacher) {
        this.assignedTeacher = assignedTeacher;
    }

    // Identifiable interface methods
    @Override
    public String getId() {
        return courseId;
    }

    @Override
    public String getDisplayName() {
        return courseTitle;
    }

    @Override
    public boolean isActive() {
        return courseId != null && !courseId.isBlank()
                && courseTitle != null && !courseTitle.isBlank();
    }

    // Registrable interface methods
    @Override
    public void validate() throws ValidationException {

        // Course ID: required, 4-30 characters
        if (courseId == null || courseId.isBlank()) {
            throw new ValidationException("Course ID is required.");
        }
        int idLength = courseId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Course ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Course code: required, 2-20 characters
        if (courseCode == null || courseCode.isBlank()) {
            throw new ValidationException("Course code is required.");
        }
        int codeLength = courseCode.trim().length();
        if (codeLength < 2 || codeLength > 20) {
            throw new ValidationException("Course code must be 2-20 characters.");
        }

        // Course title: required, 2-120 characters
        if (courseTitle == null || courseTitle.isBlank()) {
            throw new ValidationException("Course title is required.");
        }
        int titleLength = courseTitle.trim().length();
        if (titleLength < 2 || titleLength > 120) {
            throw new ValidationException("Course title must be 2-120 characters.");
        }

        // Credit hours: 1 to 6
        if (creditHours < 1 || creditHours > 6) {
            throw new ValidationException(
                    "Credit hours must be between 1 and 6.");
        }

        // Department: must be a real, selected Department object
        if (department == null) {
            throw new ValidationException(
                    "A course must belong to a department.");
        }

        // Assigned teacher: must be a real, active Teacher object
        if (assignedTeacher == null) {
            throw new ValidationException(
                    "A course must have an assigned teacher.");
        }
        if (!assignedTeacher.isActive()) {
            throw new ValidationException(
                    "The assigned teacher must be active.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("COURSE");
        System.out.println("  01 Course ID       : " + courseId);
        System.out.println("  02 Course code     : " + courseCode);
        System.out.println("  03 Course title    : " + courseTitle);
        System.out.println("  04 Credit hours    : " + creditHours);
        System.out.println("  05 Department      : " + (department == null
                ? "Not assigned"
                : "[" + department.getId() + "] " + department.getDisplayName()));
        System.out.println("  06 Assigned teacher: " + (assignedTeacher == null
                ? "Not assigned"
                : "[" + assignedTeacher.getId() + "] "
                + assignedTeacher.getDisplayName()));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + courseId + "] " + courseCode + " - " + courseTitle;
    }

    // Readable text for the object
    @Override
    public String toString() {
        return "Course{courseId='" + courseId + "', code='" + courseCode
                + "', title='" + courseTitle + "', creditHours=" + creditHours
                + ", department=" + (department == null
                ? "none" : department.getDisplayName())
                + ", teacher=" + (assignedTeacher == null
                ? "none" : assignedTeacher.getDisplayName()) + "}";
    }
}
