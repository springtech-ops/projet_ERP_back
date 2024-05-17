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
import org.springtech.springmarket.enumeration.ProductStatus;
import org.springtech.springmarket.handler.AgencyNotFoundException;
import org.springtech.springmarket.repository.*;
import org.springtech.springmarket.service.ProductService;

import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FournisseurRepository fournisseurRepository;
    private final AgencyRepository agencyRepository;
    private final StockRepository stockRepository;
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Product createProduct(Product product) {
        product.setStatus(String.valueOf(ProductStatus.EMPTY));
        product.setIsActive(false);
        product.setPrixAchat(0);
        product.setPrixVente(0);
        product.setQuantity(0);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Page<Product> getProducts(int page, int size, String agencyCode) {
        if (agencyCode == null || agencyCode.isEmpty()) {
            return productRepository.findAll(of(page, size));
        } else {
            Agency agency = agencyRepository.findByCode(agencyCode);
            if (agency != null) {
                return productRepository.findByAgency(agency,of(page, size));
            } else {
                throw new AgencyNotFoundException("Agency not found with code: " + agencyCode);
            }
        }
    }

    @Override
    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public Page<Product> searchProducts(String name, String agencyCode, int page, int size) {
        if (agencyCode == null || agencyCode.isEmpty()) {
            return productRepository.findByNameContaining(name, of(page, size));
        } else {
            Agency agency = agencyRepository.findByCode(agencyCode);
            if (agency != null) {
                return productRepository.findByNameContainingAndAgency(name, agency, of(page, size));
            } else {
                throw new AgencyNotFoundException("Agency not found with code: " + agencyCode);
            }
        }
    }

    @Override
    public void addProductToCategory(Long id, Product product) {
        Category category = categoryRepository.findById(id).get();
        product.setCategory(category);
        productRepository.save(product);
    }

    @Override
    public void addProductToFournisseur(Long id, Product product) {
        Fournisseur fournisseur = fournisseurRepository.findById(id).get();
        product.setFournisseur(fournisseur);
        productRepository.save(product);
    }

    @Override
    public void addProductToAgency(Long id, Product product) {
        Agency agency = agencyRepository.findById(id).get();
        product.setAgency(agency);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        long count = stockRepository.countByProductId(id);
        if (count > 0) {
            throw new IllegalStateException("Cannot delete products with associated stocks");
        }
        productRepository.deleteById(id);
    }

    public List<Product> getProducts(String agencyCode) {
            return productRepository.findByAgencyCode(agencyCode);
    }
}
