package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String displayAdminPanel(Model model) {
        List<User> users = userService.getAllUsers();
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("allRoles", roles);
        return "admin/admin-panel";
    }

    @PostMapping("/users")
    public String createUser(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam int age,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(value = "selectedRoles", required = false) List<Long> roleIds,
                             RedirectAttributes redirectAttributes) {
        try {
            UserDto userDto = new UserDto();
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setAge(age);
            userDto.setEmail(email);
            userDto.setPassword(password);
            userDto.setRoleIds(roleIds != null ? java.util.Set.copyOf(roleIds) : null);

            userService.createUserFromDto(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam int age,
                             @RequestParam String email,
                             @RequestParam(required = false) String password,
                             @RequestParam(value = "selectedRoles", required = false) List<Long> roleIds,
                             RedirectAttributes redirectAttributes) {
        try {
            UserDto userDto = new UserDto();
            userDto.setId(id);
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setAge(age);
            userDto.setEmail(email);
            userDto.setPassword(password);
            userDto.setRoleIds(roleIds != null ? java.util.Set.copyOf(roleIds) : null);

            userService.updateUserFromDto(id, userDto);
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