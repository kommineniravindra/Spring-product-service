package com.sathya.controller;

import com.sathya.entity.Product;
import com.sathya.feign.AuthClient;
import com.sathya.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final AuthClient authClient;

    // 🟢 Public endpoints
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // 🔐 Admin-only endpoints
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product,
                                           @RequestHeader("Authorization") String token) {
        if (!isAdmin(token)) return unauthorized();
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody Product product,
                                           @RequestHeader("Authorization") String token) {
        if (!isAdmin(token)) return unauthorized();
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id,
                                           @RequestHeader("Authorization") String token) {
        if (!isAdmin(token)) return unauthorized();
        productService.deleteProduct(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // 🔧 Helper methods
    private boolean isAdmin(String token) {
        try {
            // Clean token: remove "Bearer " if it exists
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String role = authClient.validateToken(token);
            return role.equalsIgnoreCase("ADMIN");
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    private ResponseEntity<String> unauthorized() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access Denied: Only admins can perform this operation.");
    }
}
