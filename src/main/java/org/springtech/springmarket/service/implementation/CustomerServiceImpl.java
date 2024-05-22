package org.springtech.springmarket.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.Customer;
import org.springtech.springmarket.domain.Stats;
import org.springtech.springmarket.query.CustomerQuery;
import org.springtech.springmarket.repository.CustomerRepository;
import org.springtech.springmarket.repository.InvoiceRepository;
import org.springtech.springmarket.rowMapper.StatsRowMapper;
import org.springtech.springmarket.service.CustomerService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.domain.PageRequest.of;
import static org.springtech.springmarket.query.CustomerQuery.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final NamedParameterJdbcTemplate jdbc;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(new Date());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> getCustomers(int page, int size) {
        return customerRepository.findAll(of(page, size));
    }

    @Override
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public Page<Customer> searchCustomers(String name, int page, int size) {
        return customerRepository.findByNameContaining(name, of(page, size));
    }

    @Override
    public void deleteCustomer(Long id) {
        // Vérifie d'abord si le fournisseur existe
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found with id: " + id);
        }
        // Vérifie si des stocks sont associés à ce fournisseur
        long count = invoiceRepository.countByCustomerId(id);
        if (count > 0) {
            throw new IllegalStateException("Cannot delete fournisseur with associated stocks");
        }
        // Supprime le fournisseur
        customerRepository.deleteById(id);
    }

//    @Override
//    public Stats getStats() {
//        return jdbc.queryForObject(STATS_QUERY, Map.of(), new StatsRowMapper());
//    }

    public Stats getStats(String date, String monthYear, String year, boolean week) {
        String query;
        Object[] params;

        if (date != null) {
            query = CustomerQuery.STATS_QUERY_DATE;
            params = new Object[]{"%" + date + "%", "%" + date + "%", "%" + date + "%", "%" + date + "%", "%" + date + "%", "%" + date + "%"};
        } else if (monthYear != null) {
            query = CustomerQuery.STATS_QUERY_MONTH_YEAR;
            params = new Object[]{monthYear + "%", monthYear + "%", monthYear + "%", monthYear + "%", monthYear + "%", monthYear + "%"};
        } else if (year != null) {
            query = CustomerQuery.STATS_QUERY_YEAR;
            params = new Object[]{year + "%", year + "%", year + "%", year + "%", year + "%", year + "%"};
        } else if (week) {
            query = CustomerQuery.STATS_QUERY_LAST_DAYS;
            params = new Object[]{};
        } else {
            query = CustomerQuery.STATS_QUERY;
            params = new Object[]{};
        }

        // Passez la requête lors de la création de l'objet StatsRowMapper
        return jdbcTemplate.queryForObject(query, params, new StatsRowMapper(query));
    }



}
