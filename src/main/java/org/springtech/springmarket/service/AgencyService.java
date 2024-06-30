package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Product;
import org.springtech.springmarket.domain.Stock;

public interface AgencyService {
    // Agency functions
    Agency createAgency(Agency agency);
    Agency updateAgency(Agency agency);
    Page<Agency> getAgencies(int page, int size);
    Iterable<Agency> getAgencies();
    Agency getAgency(Long id);

    void addAgencyToProduct(Long id, Product product);
    Page<Agency> searchAgencies(String name, int page, int size);

    void deleteAgency(Long id);

    String findAgencyNameByCode(String code);

    String findAgencyNameById(Long id);
}


