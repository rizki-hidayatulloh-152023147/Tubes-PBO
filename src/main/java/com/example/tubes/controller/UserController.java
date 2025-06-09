package com.example.tubes.controller;

import com.example.tubes.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "user/home"; // mengarah ke templates/user/home.html
    }
}
