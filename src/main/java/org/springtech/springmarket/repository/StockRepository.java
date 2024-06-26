package org.springtech.springmarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Stock;

public interface StockRepository extends PagingAndSortingRepository<Stock, Long>, ListCrudRepository<Stock, Long> {


    @Query("SELECT COUNT(s) FROM Stock s WHERE s.product.id = :id")
    long countByProductId(@Param("id") Long id);

    Page<Stock> findByStockNumberContaining(String stockNumber, Pageable pageable);
}
