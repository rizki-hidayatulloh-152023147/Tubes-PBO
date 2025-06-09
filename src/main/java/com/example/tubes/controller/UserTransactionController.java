package com.example.tubes.controller;
import com.example.tubes.repository.TransactionRepository;
import com.example.tubes.repository.ProductRepository;
import com.example.tubes.repository.UserRepository;
import com.example.tubes.model.Product;
import com.example.tubes.model.Transaction;
import com.example.tubes.model.User;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/user")
public class UserTransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Tampilkan daftar produk yang bisa dibeli user
    @GetMapping("/shop")
    public String shopPage(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "user/shop"; // user lihat semua produk
    }

    // Form pembelian produk
    @GetMapping("/buy/{id}")
    public String showBuyForm(@PathVariable("id") Long productId, Model model) {
        Product product = productRepository.findById(productId).orElse(null);
        model.addAttribute("product", product);
        model.addAttribute("transaction", new Transaction());
        return "user/buy_form";
    }

    // Proses simpan transaksi
    @PostMapping("/buy")
    public String processBuy(
        @ModelAttribute Transaction transaction,
        @RequestParam("productId") Long productId,
        @RequestParam("userId") Long userId // bisa juga ambil dari sesi login nanti
    ) {
        Product product = productRepository.findById(productId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (product != null && user != null) {
            transaction.setProduct(product);
            transaction.setUser(user);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setTotalPrice(product.getPrice() * transaction.getQuantity());
            transactionRepository.save(transaction);
        }

        return "redirect:/user/transactions";
    }
    @GetMapping("/transaction/edit/{id}")
    public String editTransactionForm(@PathVariable("id") Long id, Model model) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            return "redirect:/user/transactions";
        }
        model.addAttribute("transaction", transaction);
        return "user/edit_transaction";
    }
    @PostMapping("/transaction/edit")
    public String processEditTransaction(@ModelAttribute Transaction transaction) {
        Product product = productRepository.findById(transaction.getProduct().getId()).orElse(null);
        if (product != null) {
            transaction.setTotalPrice(product.getPrice() * transaction.getQuantity());
            transactionRepository.save(transaction);
        }
        return "redirect:/user/transactions";
    }
    @GetMapping("/transaction/delete/{id}")
    public String deleteTransaction(@PathVariable("id") Long id) {
        transactionRepository.deleteById(id);
        return "redirect:/user/transactions";
    }


    // Lihat daftar transaksi user
    @GetMapping("/transactions")
    public String viewUserTransactions(Model model) {
        model.addAttribute("transactions", transactionRepository.findAll()); // bisa difilter per user nanti
        return "user/transactions";
    }
}
