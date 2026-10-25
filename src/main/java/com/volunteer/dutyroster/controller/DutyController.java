package com.volunteer.dutyroster.controller;

import com.volunteer.dutyroster.model.Duty;
import com.volunteer.dutyroster.model.DutyStatus;
import com.volunteer.dutyroster.model.User;
import com.volunteer.dutyroster.model.UserRole;
import com.volunteer.dutyroster.service.DutyService;
import com.volunteer.dutyroster.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for CRUD operations on Duty records.
 * Handles create, view, update, delete, search and status workflow.
 */
@Controller
@RequestMapping("/duties")
public class DutyController {

    private final DutyService dutyService;
    private final UserService userService;

    @Autowired
    public DutyController(DutyService dutyService, UserService userService) {
        this.dutyService = dutyService;
        this.userService = userService;
    }

    /**
     * List all duties with optional search/filter.
     */
    @GetMapping
    public String listDuties(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) DutyStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            Model model) {

        List<Duty> duties;
        if (keyword != null || status != null || date != null) {
            duties = dutyService.searchAdvanced(keyword, status, date);
            model.addAttribute("keyword", keyword);
            model.addAttribute("filterStatus", status);
            model.addAttribute("filterDate", date);
        } else {
            duties = dutyService.findAll();
        }

        model.addAttribute("duties", duties);
        model.addAttribute("statuses", DutyStatus.values());
        return "duties/list";
    }

    /**
     * Show create duty form.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("duty", new Duty());
        model.addAttribute("volunteers", userService.findActiveVolunteers());
        model.addAttribute("statuses", DutyStatus.values());
        model.addAttribute("editing", false);
        return "duties/form";
    }

    /**
     * Handle duty creation form submission.
     */
    @PostMapping("/create")
    public String createDuty(@Valid @ModelAttribute("duty") Duty duty,
                             BindingResult result,
                             @RequestParam(required = false) Long volunteerId,
                             Authentication authentication,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            System.err.println("[DUTY CREATE ERROR] " + result.getAllErrors());
            model.addAttribute("volunteers", userService.findActiveVolunteers());
            model.addAttribute("statuses", DutyStatus.values());
            return "duties/form";
        }

        // Set the creator
        User creator = userService.findByUsername(authentication.getName()).orElse(null);
        duty.setCreatedBy(creator);

        // Assign volunteer if selected
        if (volunteerId != null) {
            User volunteer = userService.findById(volunteerId).orElse(null);
            duty.setVolunteer(volunteer);
            if (volunteer != null) {
                duty.setStatus(DutyStatus.ASSIGNED);
            }
        }

        dutyService.createDuty(duty);
        redirectAttributes.addFlashAttribute("successMessage", "Duty created successfully!");
        return "redirect:/duties";
    }

    /**
     * Show edit duty form.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Duty duty = dutyService.findById(id).orElse(null);
        if (duty == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Duty not found!");
            return "redirect:/duties";
        }
        model.addAttribute("duty", duty);
        model.addAttribute("volunteers", userService.findActiveVolunteers());
        model.addAttribute("statuses", DutyStatus.values());
        model.addAttribute("editing", true);
        return "duties/form";
    }

    /**
     * Handle duty update form submission.
     */
    @PostMapping("/edit/{id}")
    public String updateDuty(@PathVariable Long id,
                             @Valid @ModelAttribute("duty") Duty duty,
                             BindingResult result,
                             @RequestParam(required = false) Long volunteerId,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("volunteers", userService.findActiveVolunteers());
            model.addAttribute("statuses", DutyStatus.values());
            model.addAttribute("editing", true);
            return "duties/form";
        }

        // Assign volunteer if selected
        if (volunteerId != null) {
            User volunteer = userService.findById(volunteerId).orElse(null);
            duty.setVolunteer(volunteer);
        }

        dutyService.updateDuty(id, duty);
        redirectAttributes.addFlashAttribute("successMessage", "Duty updated successfully!");
        return "redirect:/duties";
    }

    /**
     * View duty details.
     */
    @GetMapping("/view/{id}")
    public String viewDuty(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Duty duty = dutyService.findById(id).orElse(null);
        if (duty == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Duty not found!");
            return "redirect:/duties";
        }
        model.addAttribute("duty", duty);
        return "duties/view";
    }

    /**
     * Update duty status (role-based workflow).
     */
    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam DutyStatus status,
                               RedirectAttributes redirectAttributes) {
        dutyService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Status updated to " + status.getDisplayName());
        return "redirect:/duties/view/" + id;
    }

    /**
     * Delete a duty.
     */
    @PostMapping("/delete/{id}")
    public String deleteDuty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        dutyService.deleteDuty(id);
        redirectAttributes.addFlashAttribute("successMessage", "Duty deleted successfully!");
        return "redirect:/duties";
    }

    /**
     * Search duties page.
     */
    @GetMapping("/search")
    public String searchDuties(@RequestParam(required = false) String q, Model model) {
        if (q != null && !q.trim().isEmpty()) {
            model.addAttribute("duties", dutyService.search(q));
            model.addAttribute("searchQuery", q);
        }
        model.addAttribute("statuses", DutyStatus.values());
        return "duties/search";
    }
}
