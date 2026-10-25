package com.volunteer.dutyroster.controller;

import com.volunteer.dutyroster.model.User;
import com.volunteer.dutyroster.model.UserRole;
import com.volunteer.dutyroster.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for managing volunteer/user records.
 * Supports CRUD operations on users for Admin role.
 */
@Controller
@RequestMapping("/volunteers")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * List all volunteers.
     */
    @GetMapping
    public String listVolunteers(@RequestParam(required = false) String search, Model model) {
        List<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchByName(search);
            model.addAttribute("search", search);
        } else {
            users = userService.findAll();
        }
        model.addAttribute("users", users);
        return "volunteers/list";
    }

    /**
     * Show create volunteer form.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", UserRole.values());
        return "volunteers/form";
    }

    /**
     * Handle volunteer creation.
     */
    @PostMapping("/create")
    public String createVolunteer(@Valid @ModelAttribute("user") User user,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", UserRole.values());
            return "volunteers/form";
        }

        if (userService.usernameExists(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            model.addAttribute("roles", UserRole.values());
            return "volunteers/form";
        }

        userService.createUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Volunteer created successfully!");
        return "redirect:/volunteers";
    }

    /**
     * Show edit volunteer form.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/volunteers";
        }
        user.setPassword(""); // Don't send password back
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("editing", true);
        return "volunteers/form";
    }

    /**
     * Handle volunteer update.
     */
    @PostMapping("/edit/{id}")
    public String updateVolunteer(@PathVariable Long id,
                                  @Valid @ModelAttribute("user") User user,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        // Allow empty password on edit (means don't change)
        if (user.getPassword() != null && user.getPassword().isEmpty()) {
            user.setPassword(null);
        }

        if (result.hasErrors() && !(result.getFieldError("password") != null && (user.getPassword() == null || user.getPassword().isEmpty()))) {
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("editing", true);
            return "volunteers/form";
        }

        userService.updateUser(id, user);
        redirectAttributes.addFlashAttribute("successMessage", "Volunteer updated successfully!");
        return "redirect:/volunteers";
    }

    /**
     * View volunteer details.
     */
    @GetMapping("/view/{id}")
    public String viewVolunteer(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found!");
            return "redirect:/volunteers";
        }
        model.addAttribute("user", user);
        return "volunteers/view";
    }

    /**
     * Delete a volunteer.
     */
    @PostMapping("/delete/{id}")
    public String deleteVolunteer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "Volunteer deleted successfully!");
        return "redirect:/volunteers";
    }
}
