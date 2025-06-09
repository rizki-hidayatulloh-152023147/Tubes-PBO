package com.example.tubes.controller;

import com.example.tubes.model.Transaction;
import com.example.tubes.repository.ProductRepository;
import com.example.tubes.repository.TransactionRepository;
import com.example.tubes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/admin/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // List semua transaksi
    @GetMapping
    public String listTransactions(Model model) {
        model.addAttribute("transactions", transactionRepository.findAll());
        return "admin/transactions";
    }

    // Tampilkan form tambah transaksi
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "admin/transaction_form";
    }

    // Simpan transaksi baru / update transaksi
@PostMapping("/save")
public String saveTransaction(@ModelAttribute Transaction transaction) {
    if (transaction.getId() == null) {
        transaction.setTransactionDate(LocalDateTime.now());
    } else {
        // buat jaga-jaga kalau transactionDate null saat edit
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }
    }

    transaction.setProduct(productRepository.findById(transaction.getProductId()).orElse(null));
    transaction.setUser(userRepository.findById(transaction.getUserId()).orElse(null));

    transactionRepository.save(transaction);
    return "redirect:/admin/transactions";
}
    // Tampilkan form edit transaksi
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(id);
        if (transactionOpt.isPresent()) {
            model.addAttribute("transaction", transactionOpt.get());
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("users", userRepository.findAll());
            return "admin/transaction_form";
        } else {
            return "redirect:/admin/transactions";
        }
    }

    // Hapus transaksi
    @GetMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
        return "redirect:/admin/transactions";
    }
}