package org.springtech.springmarket.service.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
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
import org.springtech.springmarket.handler.ResourceNotFoundException;
import org.springtech.springmarket.repository.*;
import org.springtech.springmarket.service.ProductService;

import java.util.Date;
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

    @Override
    public Product createProduct(Product product) {
        product.setStatus(String.valueOf(ProductStatus.EMPTY));
        product.setIsActive(false);
        product.setPrixAchat(0);
        product.setPrixVente(0);
        product.setQuantity(0);
        product.setUpdateAt(new Date());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product updateRequest) {
        Product existingProduct = productRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (updateRequest.getName() != null) existingProduct.setName(updateRequest.getName());
        if (updateRequest.getCode() != null) existingProduct.setCode(updateRequest.getCode());
        if (updateRequest.getDescription() != null) existingProduct.setDescription(updateRequest.getDescription());
        if (updateRequest.getIsActive() != null) existingProduct.setIsActive(updateRequest.getIsActive());

        return productRepository.save(existingProduct);
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

    @Transactional
    public void addProductToEntities(Long agencyId, Long fournisseurId, Long categoryId, Product product) {
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Agency not found with id: " + agencyId));
        product.setAgency(agency);

        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur not found with id: " + fournisseurId));
        product.setFournisseur(fournisseur);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        product.setCategory(category);

        product.setStatus(String.valueOf(ProductStatus.EMPTY));
        product.setIsActive(false);
        product.setPrixAchat(0);
        product.setPrixVente(0);
        product.setQuantity(0);
        product.setUpdateAt(new Date());

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

    public List<Product> getProductsCodeAndStatus(String agencyCode) {
        if (agencyCode == null || agencyCode.isEmpty()) {
            return productRepository.findByIsActiveTrue();
        } else {
            return productRepository.findByAgency_CodeAndIsActiveTrue(agencyCode);
        }
    }
}
