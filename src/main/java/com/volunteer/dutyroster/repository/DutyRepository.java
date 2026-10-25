package com.volunteer.dutyroster.repository;

import com.volunteer.dutyroster.model.Duty;
import com.volunteer.dutyroster.model.DutyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Duty entity.
 */
@Repository
public interface DutyRepository extends JpaRepository<Duty, Long> {

    List<Duty> findByStatus(DutyStatus status);

    List<Duty> findByVolunteerId(Long volunteerId);

    List<Duty> findByDutyDate(LocalDate date);

    List<Duty> findByDutyDateBetween(LocalDate startDate, LocalDate endDate);

    List<Duty> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT d FROM Duty d WHERE d.volunteer.fullName LIKE %:name%")
    List<Duty> findByVolunteerName(@Param("name") String volunteerName);

    @Query("SELECT d FROM Duty d WHERE " +
           "(:keyword IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(d.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Duty> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT d FROM Duty d WHERE " +
           "(:keyword IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(d.location) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR d.status = :status) " +
           "AND (:date IS NULL OR d.dutyDate = :date)")
    List<Duty> searchDuties(@Param("keyword") String keyword,
                            @Param("status") DutyStatus status,
                            @Param("date") LocalDate date);

    long countByStatus(DutyStatus status);

    @Query("SELECT d FROM Duty d WHERE d.dutyDate >= :today ORDER BY d.dutyDate ASC")
    List<Duty> findUpcomingDuties(@Param("today") LocalDate today);

    List<Duty> findAllByOrderByDutyDateDesc();
}
