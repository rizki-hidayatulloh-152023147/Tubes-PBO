package com.example.tubes.controller;

import com.example.tubes.model.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    // Menampilkan halaman keranjang
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        
        double totalPrice = cartItems.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        
        return "user/cart"; // Ganti dengan path ke template keranjang
    }

    // Menambah produk ke keranjang
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam String productName,
                            @RequestParam Double productPrice,
                            @RequestParam Integer quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        
        // Cek apakah produk sudah ada di keranjang
        CartItem existingItem = cartItems.stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst()
            .orElse(null);
        
        if (existingItem != null) {
            // Update jumlah jika sudah ada
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Tambah item baru
            CartItem newItem = new CartItem(productId, productName, productPrice, quantity);
            cartItems.add(newItem);
        }
        
        // Simpan ke session
        session.setAttribute("cartItems", cartItems);
        session.setAttribute("cartCount", cartItems.size());
        
        redirectAttributes.addFlashAttribute("success", "Produk berhasil ditambah ke keranjang!");
        return "redirect:/"; // Kembali ke halaman utama
    }

    // Menghapus produk dari keranjang
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems != null) {
            cartItems.removeIf(item -> item.getProductId().equals(productId));
            session.setAttribute("cartItems", cartItems);
            session.setAttribute("cartCount", cartItems.size());
        }
        
        redirectAttributes.addFlashAttribute("success", "Produk berhasil dihapus dari keranjang!");
        return "redirect:/cart"; // Kembali ke halaman keranjang
    }
}