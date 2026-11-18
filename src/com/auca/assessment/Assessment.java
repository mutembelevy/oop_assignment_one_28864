
package com.auca.assessment;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;
import com.auca.enrollment.Enrollment;

import java.time.LocalDate;

public class Assessment implements Identifiable, Registrable {

    // Six Assessment attributes
    private final String assessmentId;
    private Enrollment enrollment;
    private String assessmentType;
    private double maximumScore;
    private double obtainedScore;
    private LocalDate assessmentDate;

    // Constructor
    public Assessment(
            String assessmentId,
            Enrollment enrollment,
            String assessmentType,
            double maximumScore,
            double obtainedScore,
            LocalDate assessmentDate) {

        this.assessmentId = assessmentId;
        this.enrollment = enrollment;
        this.assessmentType = assessmentType;
        this.maximumScore = maximumScore;
        this.obtainedScore = obtainedScore;
        this.assessmentDate = assessmentDate;
    }

    // Getters
    public String getAssessmentId() {
        return assessmentId;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public double getMaximumScore() {
        return maximumScore;
    }

    public double getObtainedScore() {
        return obtainedScore;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    // Setters
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public void setMaximumScore(double maximumScore) {
        this.maximumScore = maximumScore;
    }

    public void setObtainedScore(double obtainedScore) {
        this.obtainedScore = obtainedScore;
    }

    public void setAssessmentDate(LocalDate assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    // Identifiable interface methods
    @Override
    public String getId() {
        return assessmentId;
    }

    @Override
    public String getDisplayName() {
        return assessmentType + " - " + assessmentId;
    }

    @Override
    public boolean isActive() {
        return enrollment != null && enrollment.isActive();
    }
    // Registrable interface methods
    @Override
    public void validate() throws ValidationException {

        // Assessment ID: required, 4-30 characters
        if (assessmentId == null || assessmentId.isBlank()) {
            throw new ValidationException("Assessment ID is required.");
        }
        int idLength = assessmentId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Assessment ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Enrollment: must be a real, ACTIVE enrollment
        if (enrollment == null) {
            throw new ValidationException("Enrollment is required.");
        }
        if (!enrollment.isActive()) {
            throw new ValidationException(
                    "Assessment requires an active enrollment.");
        }

        // Assessment type: only QUIZ, ASSIGNMENT, MIDTERM or FINAL_EXAM
        if (assessmentType == null || assessmentType.isBlank()) {
            throw new ValidationException("Assessment type is required.");
        }
        if (!assessmentType.equals("QUIZ")
                && !assessmentType.equals("ASSIGNMENT")
                && !assessmentType.equals("MIDTERM")
                && !assessmentType.equals("FINAL_EXAM")) {
            throw new ValidationException(
                    "Assessment type must be QUIZ, ASSIGNMENT, MIDTERM or FINAL_EXAM.");
        }

        // Maximum score: a real number greater than zero
        if (Double.isNaN(maximumScore) || Double.isInfinite(maximumScore)
                || maximumScore <= 0) {
            throw new ValidationException(
                    "Maximum score must be greater than zero.");
        }

        // Obtained score: a real number from zero up to the maximum score
        if (Double.isNaN(obtainedScore) || obtainedScore < 0
                || obtainedScore > maximumScore) {
            throw new ValidationException(
                    "Obtained score must be between zero and maximum score.");
        }

        // Assessment date: required and not in the future
        if (assessmentDate == null) {
            throw new ValidationException("Assessment date is required.");
        }
        if (assessmentDate.isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Assessment date must not be in the future.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("ASSESSMENT");
        System.out.println("  01 Assessment ID   : " + assessmentId);
        System.out.println("  02 Enrollment      : " + (enrollment == null
                ? "Not assigned"
                : "[" + enrollment.getId() + "] " + enrollment.getDisplayName()));
        System.out.println("  03 Assessment type : " + assessmentType);
        System.out.println("  04 Maximum score   : " + maximumScore);
        System.out.println("  05 Obtained score  : " + obtainedScore);
        System.out.println("  06 Assessment date : " + assessmentDate);
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + assessmentId + "] " + assessmentType + " "
                + obtainedScore + "/" + maximumScore
                + (enrollment == null ? "" : " - " + enrollment.getDisplayName());
    }

    // Readable text for the object
    @Override
    public String toString() {
        return "Assessment{assessmentId='" + assessmentId + "', enrollment="
                + (enrollment == null ? "none" : enrollment.getId())
                + ", type='" + assessmentType + "', maximumScore=" + maximumScore
                + ", obtainedScore=" + obtainedScore
                + ", date=" + assessmentDate + "}";
    }
}
