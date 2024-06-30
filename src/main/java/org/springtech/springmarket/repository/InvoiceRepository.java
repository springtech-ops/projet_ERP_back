package org.springtech.springmarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.Product;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long>, ListCrudRepository<Invoice, Long> {
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.customer.id = :id")
    long countByCustomerId(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Invoice i WHERE i.id = :id")
    void deleteById(@Param("id") Long id);

    Page<Invoice> findByInvoiceNumberContainingAndAgencyCode(String invoiceNumber, String agencyCode, Pageable pageable);
    Page<Invoice> findByInvoiceNumberContaining(String invoiceNumber, Pageable pageable);
    Page<Invoice> findByAgencyCode(String agencyCode, Pageable pageable);
}
