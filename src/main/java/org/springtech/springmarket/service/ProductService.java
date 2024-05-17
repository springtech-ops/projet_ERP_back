package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.Product;

import java.util.List;

public interface ProductService {
    // Product functions
    Product createProduct(Product product);
    Product updateProduct(Product product);
    Page<Product> getProducts(int page, int size, String agencyCode);
    Iterable<Product> getProducts();
    Product getProduct(Long id);
    Page<Product> searchProducts(String name, String agencyCode, int page, int size);

    void deleteProduct(Long id);

    List<Product> getProducts(String agencyCode);

    void addProductToCategory(Long id, Product product);
    void addProductToFournisseur(Long id, Product product);
    void addProductToAgency(Long id, Product product);
}
