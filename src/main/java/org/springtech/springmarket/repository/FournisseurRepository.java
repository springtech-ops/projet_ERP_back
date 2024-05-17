package org.springtech.springmarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Fournisseur;

public interface FournisseurRepository extends PagingAndSortingRepository<Fournisseur, Long>, ListCrudRepository<Fournisseur, Long>  {
    Page<Fournisseur> findByNameContaining(String name, Pageable pageable);
}
