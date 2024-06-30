package org.springtech.springmarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Product;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, ListCrudRepository<Product, Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.id = :id")
    long countByCategoryId(@Param("id") Long id);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.fournisseur.id = :id")
    long countByFournisseurId(@Param("id") Long id);

    Page<Product> findByAgency(Agency agency, Pageable pageable);

    Page<Product> findByNameContainingAndAgency(String name, Agency agency, PageRequest of);

    List<Product> findByAgencyCode(String agencyCode);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.agency.id = :id")
    long countByAgencyId(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :id")
    void deleteById(@Param("id") Long id);

    List<Product> findByAgency_CodeAndIsActiveTrue(String code);
    List<Product> findByIsActiveTrue();


}
