package org.springtech.springmarket.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Product;
import org.springtech.springmarket.domain.Stock;
import org.springtech.springmarket.repository.AgencyRepository;
import org.springtech.springmarket.repository.ProductRepository;
import org.springtech.springmarket.repository.StockRepository;
import org.springtech.springmarket.service.AgencyService;

import java.util.Date;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AgencyServiceImpl implements AgencyService {
    private final AgencyRepository agencyRepository;
    private final ProductRepository productRepository;
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Agency createAgency(Agency agency) {
        agency.setCreatedAt(new Date());
        return agencyRepository.save(agency);
    }

    @Override
    public Agency updateAgency(Agency agency) {
        return agencyRepository.save(agency);
    }

    @Override
    public Page<Agency> getAgencies(int page, int size) {
        return agencyRepository.findAll(of(page, size));
    }

    @Override
    public Iterable<Agency> getAgencies() {
        return agencyRepository.findAll();
    }

    @Override
    public Agency getAgency(Long id) {
        return agencyRepository.findById(id).get();
    }

    @Override
    public Page<Agency> searchAgencies(String name, int page, int size) {
        return agencyRepository.findByNameContaining(name, of(page, size));
    }

    @Override
    public void addAgencyToProduct(Long id, Product product) {
        Agency agency = agencyRepository.findById(id).get();
        product.setAgency(agency);
        productRepository.save(product);
    }

    @Override
    public void deleteAgency(Long id) {
        // Vérifie d'abord si le fournisseur existe
        if (!agencyRepository.existsById(id)) {
            throw new EntityNotFoundException("Agency not found with id: " + id);
        }
        // Vérifie si des stocks sont associés à ce fournisseur
        long count = productRepository.countByAgencyId(id);
        if (count > 0) {
            throw new IllegalStateException("Cannot delete fournisseur with associated stocks");
        }
        // Supprime le fournisseur
        agencyRepository.deleteById(id);
    }

    public String findAgencyNameByCode(String code) {
        return agencyRepository.findNameByCode(code);
    }

    public String findAgencyNameById(Long id) {
        return agencyRepository.findNameById(id);
    }

}
