package com.auca.exceptions;

public class PrerequisiteMissingException extends RuntimeException {

    private final String missingEntity;

    // Message only: the missing entity is unknown
    public PrerequisiteMissingException(String message) {
        super(message);
        this.missingEntity = "Unknown";
    }

    // Message plus the name of the missing entity, e.g. "Department"
    public PrerequisiteMissingException(String missingEntity, String message) {
        super(message);
        this.missingEntity = missingEntity;
    }

    public String getMissingEntity() {
        return missingEntity;
    }
}