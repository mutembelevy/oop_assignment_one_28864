
package com.auca.enrollment;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;
import com.auca.student.Student;
import com.auca.course.Course;

import java.time.LocalDate;

public class Enrollment implements Identifiable, Registrable {

    // Six Enrollment attributes
    private final String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String semester;
    private String enrollmentStatus;

    // Constructor
    public Enrollment(
            String enrollmentId,
            Student student,
            Course course,
            LocalDate enrollmentDate,
            String semester,
            String enrollmentStatus) {

        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.semester = semester;
        this.enrollmentStatus = enrollmentStatus;
    }

    // Getters
    public String getEnrollmentId() {
        return enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public String getSemester() {
        return semester;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    // Setters
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    // Identifiable interface methods
    @Override
    public String getId() {
        return enrollmentId;
    }

    @Override
    public String getDisplayName() {
        return student == null
                ? "Enrollment " + enrollmentId
                : student.getDisplayName() + " - "
                + (course == null ? "No course" : course.getCourseTitle());
    }

    @Override
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(enrollmentStatus);
    }

    // Registrable interface methods
    @Override
    public void validate() throws ValidationException {

        // Enrollment ID: required, 4-30 characters
        if (enrollmentId == null || enrollmentId.isBlank()) {
            throw new ValidationException("Enrollment ID is required.");
        }
        int idLength = enrollmentId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Enrollment ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Student and course: must be real, selected objects
        if (student == null) {
            throw new ValidationException("A student must be selected.");
        }
        if (course == null) {
            throw new ValidationException("A course must be selected.");
        }

        // Enrollment date: required and not in the future
        if (enrollmentDate == null) {
            throw new ValidationException("Enrollment date is required.");
        }
        if (enrollmentDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Enrollment date must not be in the future.");
        }

        // Semester must match the month of the enrollment date
        if (semester == null || semester.isBlank()) {
            throw new ValidationException("Semester is required.");
        }

        int month = enrollmentDate.getMonthValue();
        boolean validSemester;

        if (semester.equalsIgnoreCase("SEMESTER_1")) {
            validSemester = month >= 9 && month <= 12;
        } else if (semester.equalsIgnoreCase("SEMESTER_2")) {
            validSemester = month >= 1 && month <= 5;
        } else if (semester.equalsIgnoreCase("SEMESTER_3")) {
            validSemester = month >= 6 && month <= 8;
        } else {
            throw new ValidationException(
                    "Semester must be SEMESTER_1, SEMESTER_2 or SEMESTER_3.");
        }

        if (!validSemester) {
            throw new ValidationException(
                    "The semester must match the month of the enrollment date.");
        }

        // Enrollment status: only ACTIVE, DROPPED or COMPLETED
        if (enrollmentStatus == null || enrollmentStatus.isBlank()) {
            throw new ValidationException("Enrollment status is required.");
        }
        if (!enrollmentStatus.equalsIgnoreCase("ACTIVE")
                && !enrollmentStatus.equalsIgnoreCase("DROPPED")
                && !enrollmentStatus.equalsIgnoreCase("COMPLETED")) {
            throw new ValidationException(
                    "Enrollment status must be ACTIVE, DROPPED or COMPLETED.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("ENROLLMENT");
        System.out.println("  01 Enrollment ID     : " + enrollmentId);
        System.out.println("  02 Student           : " + (student == null
                ? "Not assigned"
                : "[" + student.getStudentNumber() + "] "
                + student.getDisplayName()));
        System.out.println("  03 Course            : " + (course == null
                ? "Not assigned"
                : "[" + course.getCourseCode() + "] " + course.getCourseTitle()));
        System.out.println("  04 Enrollment date   : " + enrollmentDate);
        System.out.println("  05 Semester          : " + semester);
        System.out.println("  06 Enrollment status : " + enrollmentStatus);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + enrollmentId + "] " + getDisplayName()
                + " (" + semester + ", " + enrollmentStatus + ")";
    }

    // Readable text for the object
    @Override
    public String toString() {
        return "Enrollment{enrollmentId='" + enrollmentId + "', student="
                + (student == null ? "none" : student.getDisplayName())
                + ", course=" + (course == null ? "none" : course.getCourseTitle())
                + ", date=" + enrollmentDate + ", semester='" + semester
                + "', status='" + enrollmentStatus + "'}";
    }
}
