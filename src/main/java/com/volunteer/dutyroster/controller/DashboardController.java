package com.volunteer.dutyroster.controller;

import com.volunteer.dutyroster.model.UserRole;
import com.volunteer.dutyroster.service.DutyService;
import com.volunteer.dutyroster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * Controller for the main dashboard view.
 * Displays summary statistics and upcoming duties.
 */
@Controller
public class DashboardController {

    private final DutyService dutyService;
    private final UserService userService;

    @Autowired
    public DashboardController(DutyService dutyService, UserService userService) {
        this.dutyService = dutyService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Long> stats = dutyService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("upcomingDuties", dutyService.findUpcoming());
        model.addAttribute("totalVolunteers", userService.countByRole(UserRole.VOLUNTEER));
        model.addAttribute("totalCoordinators", userService.countByRole(UserRole.COORDINATOR));
        return "dashboard";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
}
