package org.springtech.springmarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springtech.springmarket.domain.Agency;

public interface AgencyRepository extends PagingAndSortingRepository<Agency, Long>, ListCrudRepository<Agency, Long> {
    Page<Agency> findByNameContaining(String name, Pageable pageable);

    Agency findByCode(String agencyCode);
}
