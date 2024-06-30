package org.springtech.springmarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.LigneCommande;
import org.springtech.springmarket.domain.Product;

public interface LigneCommandeRepository extends PagingAndSortingRepository<LigneCommande, Long>, ListCrudRepository<LigneCommande, Long> {

    @Modifying
    @Query("DELETE FROM LigneCommande l WHERE l.id = :id")
    void deleteById(@Param("id") Long id);

    Page<LigneCommande> findByNameContaining(String name, Pageable pageable);
}
