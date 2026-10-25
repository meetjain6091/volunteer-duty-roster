package com.volunteer.dutyroster.service;

import com.volunteer.dutyroster.model.Duty;
import com.volunteer.dutyroster.model.DutyStatus;
import com.volunteer.dutyroster.model.User;
import com.volunteer.dutyroster.repository.DutyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing Duty operations.
 */
@Service
public class DutyService {

    private final DutyRepository dutyRepository;

    @Autowired
    public DutyService(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    /**
     * Create a new duty with default DRAFT status.
     */
    public Duty createDuty(Duty duty) {
        if (duty.getStatus() == null) {
            duty.setStatus(DutyStatus.DRAFT);
        }
        return dutyRepository.save(duty);
    }

    /**
     * Update an existing duty.
     */
    public Duty updateDuty(Long id, Duty updatedDuty) {
        Duty existing = dutyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Duty not found with id: " + id));

        existing.setTitle(updatedDuty.getTitle());
        existing.setDescription(updatedDuty.getDescription());
        existing.setDutyDate(updatedDuty.getDutyDate());
        existing.setStartTime(updatedDuty.getStartTime());
        existing.setEndTime(updatedDuty.getEndTime());
        existing.setLocation(updatedDuty.getLocation());
        existing.setNotes(updatedDuty.getNotes());

        // Update volunteer assignment
        if (updatedDuty.getVolunteer() != null) {
            existing.setVolunteer(updatedDuty.getVolunteer());
        }

        // Update status if provided
        if (updatedDuty.getStatus() != null) {
            existing.setStatus(updatedDuty.getStatus());
        }

        return dutyRepository.save(existing);
    }

    /**
     * Update duty status following the workflow:
     * DRAFT → ASSIGNED → CONFIRMED → COMPLETED/CANCELLED
     */
    public Duty updateStatus(Long id, DutyStatus newStatus) {
        Duty duty = dutyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Duty not found with id: " + id));
        duty.setStatus(newStatus);
        return dutyRepository.save(duty);
    }

    /**
     * Assign a volunteer to a duty and set status to ASSIGNED.
     */
    public Duty assignVolunteer(Long dutyId, User volunteer) {
        Duty duty = dutyRepository.findById(dutyId)
                .orElseThrow(() -> new RuntimeException("Duty not found with id: " + dutyId));
        duty.setVolunteer(volunteer);
        duty.setStatus(DutyStatus.ASSIGNED);
        return dutyRepository.save(duty);
    }

    public Optional<Duty> findById(Long id) {
        return dutyRepository.findById(id);
    }

    public List<Duty> findAll() {
        return dutyRepository.findAllByOrderByDutyDateDesc();
    }

    public List<Duty> findByStatus(DutyStatus status) {
        return dutyRepository.findByStatus(status);
    }

    public List<Duty> findByVolunteerId(Long volunteerId) {
        return dutyRepository.findByVolunteerId(volunteerId);
    }

    public List<Duty> findUpcoming() {
        return dutyRepository.findUpcomingDuties(LocalDate.now());
    }

    /**
     * Search duties by keyword across title, description, and location.
     */
    public List<Duty> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return dutyRepository.searchByKeyword(keyword.trim());
    }

    /**
     * Advanced search with multiple filters.
     */
    public List<Duty> searchAdvanced(String keyword, DutyStatus status, LocalDate date) {
        return dutyRepository.searchDuties(keyword, status, date);
    }

    public void deleteDuty(Long id) {
        dutyRepository.deleteById(id);
    }

    /**
     * Get dashboard statistics.
     */
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", dutyRepository.count());
        stats.put("draft", dutyRepository.countByStatus(DutyStatus.DRAFT));
        stats.put("assigned", dutyRepository.countByStatus(DutyStatus.ASSIGNED));
        stats.put("confirmed", dutyRepository.countByStatus(DutyStatus.CONFIRMED));
        stats.put("completed", dutyRepository.countByStatus(DutyStatus.COMPLETED));
        stats.put("cancelled", dutyRepository.countByStatus(DutyStatus.CANCELLED));
        return stats;
    }

    public long countAll() {
        return dutyRepository.count();
    }
}
