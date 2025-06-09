package com.example.tubes.controller;

import com.example.tubes.model.Product;
import com.example.tubes.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Menampilkan semua produk
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "admin/products"; // templates/admin/products.html
    }

    // Menampilkan form tambah produk
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product_form";
    }

    // Menyimpan produk baru atau update
    @PostMapping("/save")
public String saveProduct(@ModelAttribute("product") Product product) {
    if (product.getId() != null) {
        // Update
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + product.getId()));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        productRepository.save(existingProduct);
    } else {
        // Insert baru
        productRepository.save(product);
    }

    return "redirect:/admin/products";
}

    // Menampilkan form edit
    @GetMapping("/edit/{id}")
public String showEditForm(@PathVariable Long id, Model model) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
    System.out.println("Edit Product ID: " + product.getId());
    model.addAttribute("product", product);
    return "admin/product_form";
}

    // Menghapus produk
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }
}
