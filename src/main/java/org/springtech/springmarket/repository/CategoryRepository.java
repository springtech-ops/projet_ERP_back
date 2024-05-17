package org.springtech.springmarket.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springtech.springmarket.domain.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long>, ListCrudRepository<Category, Long> {
    Page<Category> findByNameContaining(String name, Pageable pageable);
}
