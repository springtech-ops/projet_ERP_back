package org.springtech.springmarket.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springtech.springmarket.domain.LigneCommande;
import org.springtech.springmarket.domain.Product;

public interface LigneCommandeRepository extends PagingAndSortingRepository<LigneCommande, Long>, ListCrudRepository<LigneCommande, Long> {

}
