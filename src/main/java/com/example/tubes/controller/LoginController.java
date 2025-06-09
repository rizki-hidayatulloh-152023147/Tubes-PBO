package com.example.tubes.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.tubes.model.User;
import com.example.tubes.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model, HttpSession session) {
        User existingUser = userRepo.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        if (existingUser != null) {
            session.setAttribute("loggedInUser", existingUser); // simpan user ke session

            String role = existingUser.getRole();
            if ("ADMIN".equalsIgnoreCase(role)) {
                return "redirect:/admin/dashboard";
            } else if ("USER".equalsIgnoreCase(role)) {
                return "redirect:/user/shop";
            } else {
                model.addAttribute("error", "Role tidak dikenali");
                return "login";
            }
        } else {
            model.addAttribute("error", "Username atau Password salah");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        // Cek apakah username sudah digunakan
        if (userRepo.findByUsernameAndPassword(user.getUsername(), user.getPassword()) != null) {
            model.addAttribute("error", "Username sudah digunakan");
            return "register";
        }

        // Set default role jika tidak diisi
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        userRepo.save(user);
        return "redirect:/";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }


    // ✅ Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Hapus semua data session
        return "/login"; // Redirect ke halaman login
    }
}
