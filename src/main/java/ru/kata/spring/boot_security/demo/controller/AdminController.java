package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.converter.UserConverter;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserConverter userConverter;

    public AdminController(UserService userService, RoleService roleService, UserConverter userConverter) {
        this.userService = userService;
        this.roleService = roleService;
        this.userConverter = userConverter;
    }

    @GetMapping
    public String displayAdminPanel(Model model) {
        model.addAttribute("users", userService.getAllUsers())
                .addAttribute("allRoles", roleService.getAllRoles());
        return "admin/admin-panel";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
        try {
            User user = userConverter.toEntity(userDto);
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDto userDto,
                             RedirectAttributes redirectAttributes) {
        try {
            User existingUser = userService.getUserById(id);
            User updatedUser = userConverter.toEntity(userDto);
            updatedUser.setId(id);
            userService.updateUser(updatedUser);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}