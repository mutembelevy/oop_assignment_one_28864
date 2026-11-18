
package com.auca.classroom;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.exceptions.ValidationException;

public class Classroom implements Identifiable, Registrable {

    // Six Classroom attributes
    private final String classroomId;
    private String buildingName;
    private String roomNumber;
    private int capacity;
    private int floorNumber;
    private boolean available;

    // Constructor
    public Classroom(
            String classroomId,
            String buildingName,
            String roomNumber,
            int capacity,
            int floorNumber,
            boolean available) {

        this.classroomId = classroomId;
        this.buildingName = buildingName;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.floorNumber = floorNumber;
        this.available = available;
    }

    // Getters
    public String getClassroomId() {
        return classroomId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Identifiable interface methods
    @Override
    public String getId() {
        return classroomId;
    }

    @Override
    public String getDisplayName() {
        return buildingName + " - Room " + roomNumber;
    }

    @Override
    public boolean isActive() {
        return available;
    }

    // Registrable interface methods
    @Override
    public void validate() throws ValidationException {

        // Classroom ID: required, 4-30 characters
        if (classroomId == null || classroomId.isBlank()) {
            throw new ValidationException("Classroom ID is required.");
        }
        int idLength = classroomId.trim().length();
        if (idLength < MIN_ID_LENGTH || idLength > MAX_ID_LENGTH) {
            throw new ValidationException("Classroom ID must be "
                    + MIN_ID_LENGTH + "-" + MAX_ID_LENGTH + " characters.");
        }

        // Building name: required, 2-60 characters after trimming
        if (buildingName == null || buildingName.isBlank()) {
            throw new ValidationException("Building name is required.");
        }
        int buildingLength = buildingName.trim().length();
        if (buildingLength < MIN_NAME_LENGTH || buildingLength > MAX_NAME_LENGTH) {
            throw new ValidationException("Building name must be "
                    + MIN_NAME_LENGTH + "-" + MAX_NAME_LENGTH + " characters.");
        }

        // Room number: required, at most 20 characters
        if (roomNumber == null || roomNumber.isBlank()) {
            throw new ValidationException("Room number is required.");
        }
        if (roomNumber.trim().length() > 20) {
            throw new ValidationException(
                    "Room number must not exceed 20 characters.");
        }

        // Capacity: greater than zero
        if (capacity <= 0) {
            throw new ValidationException(
                    "Classroom capacity must be greater than zero.");
        }

        // Floor number: zero or greater
        if (floorNumber < 0) {
            throw new ValidationException(
                    "Floor number must be zero or greater.");
        }
    }

    @Override
    public void printDetails() {
        System.out.println("CLASSROOM");
        System.out.println("  01 Classroom ID  : " + classroomId);
        System.out.println("  02 Building name : " + buildingName);
        System.out.println("  03 Room number   : " + roomNumber);
        System.out.println("  04 Capacity      : " + capacity);
        System.out.println("  05 Floor number  : " + floorNumber);
        System.out.println("  06 Available     : " + (available ? "YES" : "NO"));
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + classroomId + "] " + buildingName + " - Room " + roomNumber
                + " (capacity " + capacity + ")";
    }

    // Readable text for the object
    @Override
    public String toString() {
        return "Classroom{classroomId='" + classroomId + "', building='"
                + buildingName + "', room='" + roomNumber + "', capacity="
                + capacity + ", floor=" + floorNumber
                + ", available=" + available + "}";
    }
}
