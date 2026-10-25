package com.volunteer.dutyroster.service;

import com.volunteer.dutyroster.model.Duty;
import com.volunteer.dutyroster.model.DutyStatus;
import com.volunteer.dutyroster.model.User;
import com.volunteer.dutyroster.model.UserRole;
import com.volunteer.dutyroster.repository.DutyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DutyServiceTest {

    @Mock
    private DutyRepository dutyRepository;

    @InjectMocks
    private DutyService dutyService;

    private Duty sampleDuty;
    private User sampleVolunteer;

    @BeforeEach
    void setUp() {
        sampleVolunteer = new User("volunteer1", "pass", "Alice Johnson", "alice@example.com", UserRole.VOLUNTEER);
        sampleDuty = new Duty("Food Distribution", "Distribute weekly food packages", LocalDate.now().plusDays(2), "City Hall");
        sampleDuty.setId(1L);
    }

    @Test
    @DisplayName("Create duty should set default status DRAFT if not provided")
    void testCreateDutyDefaultStatus() {
        sampleDuty.setStatus(null);
        when(dutyRepository.save(any(Duty.class))).thenReturn(sampleDuty);

        Duty created = dutyService.createDuty(sampleDuty);

        assertNotNull(created);
        assertEquals(DutyStatus.DRAFT, created.getStatus());
        verify(dutyRepository, times(1)).save(sampleDuty);
    }

    @Test
    @DisplayName("Update status should transition duty correctly")
    void testUpdateStatusWorkflow() {
        when(dutyRepository.findById(1L)).thenReturn(Optional.of(sampleDuty));
        when(dutyRepository.save(any(Duty.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Duty updated = dutyService.updateStatus(1L, DutyStatus.CONFIRMED);

        assertEquals(DutyStatus.CONFIRMED, updated.getStatus());
        verify(dutyRepository).save(sampleDuty);
    }

    @Test
    @DisplayName("Assign volunteer should update status to ASSIGNED")
    void testAssignVolunteer() {
        when(dutyRepository.findById(1L)).thenReturn(Optional.of(sampleDuty));
        when(dutyRepository.save(any(Duty.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Duty assigned = dutyService.assignVolunteer(1L, sampleVolunteer);

        assertEquals(sampleVolunteer, assigned.getVolunteer());
        assertEquals(DutyStatus.ASSIGNED, assigned.getStatus());
    }

    @Test
    @DisplayName("Search duties by keyword")
    void testSearchByKeyword() {
        when(dutyRepository.searchByKeyword("Food")).thenReturn(List.of(sampleDuty));

        List<Duty> results = dutyService.search("Food");

        assertEquals(1, results.size());
        assertEquals("Food Distribution", results.get(0).getTitle());
    }

    @Test
    @DisplayName("Dashboard stats should aggregate count of each status")
    void testGetDashboardStats() {
        when(dutyRepository.count()).thenReturn(10L);
        when(dutyRepository.countByStatus(DutyStatus.DRAFT)).thenReturn(2L);
        when(dutyRepository.countByStatus(DutyStatus.ASSIGNED)).thenReturn(3L);
        when(dutyRepository.countByStatus(DutyStatus.CONFIRMED)).thenReturn(2L);
        when(dutyRepository.countByStatus(DutyStatus.COMPLETED)).thenReturn(2L);
        when(dutyRepository.countByStatus(DutyStatus.CANCELLED)).thenReturn(1L);

        Map<String, Long> stats = dutyService.getDashboardStats();

        assertEquals(10L, stats.get("total"));
        assertEquals(2L, stats.get("draft"));
        assertEquals(3L, stats.get("assigned"));
        assertEquals(2L, stats.get("confirmed"));
        assertEquals(2L, stats.get("completed"));
        assertEquals(1L, stats.get("cancelled"));
    }
}
