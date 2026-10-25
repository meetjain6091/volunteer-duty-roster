package com.volunteer.dutyroster.model;

/**
 * Represents the lifecycle status of a duty assignment.
 * Flow: DRAFT → ASSIGNED → CONFIRMED → COMPLETED or CANCELLED
 */
public enum DutyStatus {
    DRAFT("Draft"),
    ASSIGNED("Assigned"),
    CONFIRMED("Confirmed"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    DutyStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
