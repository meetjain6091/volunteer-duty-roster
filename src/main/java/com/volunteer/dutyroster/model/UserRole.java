package com.volunteer.dutyroster.model;

/**
 * Represents the role of a user in the system.
 */
public enum UserRole {
    ADMIN("Administrator"),
    COORDINATOR("Coordinator"),
    VOLUNTEER("Volunteer");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
