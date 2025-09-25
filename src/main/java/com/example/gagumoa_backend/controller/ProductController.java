package com.example.gagumoa_backend.controller;

import com.example.gagumoa_backend.model.Product;
import com.example.gagumoa_backend.model.Review;
import com.example.gagumoa_backend.repository.ProductRepository;
import com.example.gagumoa_backend.service.WebScrapingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductRepository productRepository;
    private final WebScrapingService webScrapingService;

    public ProductController(ProductRepository productRepository, WebScrapingService webScrapingService) {
        this.productRepository = productRepository;
        this.webScrapingService = webScrapingService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/{id}/reviews")
    public List<Review> getProductReviews(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            String ikeaProductId = product.get().getIkeaProductId();
            return webScrapingService.scrapeReviewsForProduct(ikeaProductId);
        }
        return List.of();
    }

    @GetMapping("/image-proxy")
    public ResponseEntity<byte[]> getImage(@RequestParam String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
            MediaType contentType = response.getHeaders().getContentType();
            MediaType finalContentType = (contentType != null) ? contentType : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.status(response.getStatusCode())
                                 .contentType(finalContentType)
                                 .body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }
}