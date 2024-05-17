package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.Category;
import org.springtech.springmarket.domain.Product;

public interface CategoryService {
    // Category functions
    Category createCategory(Category category);
    Category updateCategory(Category category);
    Page<Category> getCategories(int page, int size);
    Iterable<Category> getCategories();
    Category getCategory(Long id);
    Page<Category> searchCategories(String name, int page, int size);
    void deleteCategory(Long id);

    void addCategoryToProduct(Long id, Product product);
}
