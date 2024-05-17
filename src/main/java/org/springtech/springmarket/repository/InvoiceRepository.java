package org.springtech.springmarket.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springtech.springmarket.domain.Invoice;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long>, ListCrudRepository<Invoice, Long> {
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.customer.id = :id")
    long countByCustomerId(@Param("id") Long id);
}
