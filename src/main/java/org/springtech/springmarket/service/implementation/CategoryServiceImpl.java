package org.springtech.springmarket.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Category;
import org.springtech.springmarket.domain.Fournisseur;
import org.springtech.springmarket.domain.Product;
import org.springtech.springmarket.repository.CategoryRepository;
import org.springtech.springmarket.repository.FournisseurRepository;
import org.springtech.springmarket.repository.ProductRepository;
import org.springtech.springmarket.repository.StockRepository;
import org.springtech.springmarket.service.CategoryService;

import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final NamedParameterJdbcTemplate jdbc;


    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Page<Category> getCategories(int page, int size) {
        return categoryRepository.findAll(of(page, size));
    }

    @Override
    public Iterable<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Page<Category> searchCategories(String name, int page, int size) {
        return categoryRepository.findByNameContaining(name, of(page, size));
    }

    @Override
    public void addCategoryToProduct(Long id, Product product) {
        Category category = categoryRepository.findById(id).get();
        product.setCategory(category);
        productRepository.save(product);
    }

    @Override
    public void deleteCategory(Long id) {
        // Vérifie d'abord si le fournisseur existe
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Fournisseur not found with id: " + id);
        }
        // Vérifie si des stocks sont associés à ce fournisseur
        long count = productRepository.countByCategoryId(id);
        if (count > 0) {
            throw new IllegalStateException("Cannot delete fournisseur with associated stocks");
        }
        // Supprime le fournisseur
        categoryRepository.deleteById(id);
    }
}
