package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String displayAdminPanel(Model model) {
        model.addAttribute("users", userService.getAllUsers())
                .addAttribute("allRoles", roleService.getAllRoles());
        return "admin/admin-panel";
    }

    @PostMapping("/users")
    public String createUser(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam int age,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(value = "selectedRoles", required = false) java.util.List<Long> roleIds,
                             RedirectAttributes redirectAttributes) {
        userService.createUser(firstName, lastName, age, email, password, roleIds, redirectAttributes);
        return "redirect:/admin";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam int age,
                             @RequestParam String email,
                             @RequestParam(required = false) String password,
                             @RequestParam(value = "selectedRoles", required = false) java.util.List<Long> roleIds,
                             RedirectAttributes redirectAttributes) {
        userService.updateUser(id, firstName, lastName, age, email, password, roleIds, redirectAttributes);
        return "redirect:/admin";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id, redirectAttributes);
        return "redirect:/admin";
    }
}