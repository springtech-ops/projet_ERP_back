package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.Customer;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.Stats;

public interface CustomerService {
    // Customer functions
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Page<Customer> getCustomers(int page, int size);
    Iterable<Customer> getCustomers();
    Customer getCustomer(Long id);
    Page<Customer> searchCustomers(String name, int page, int size);
    void deleteCustomer(Long id);

    Stats getStats(String date, String monthYear, String year, boolean week);
}
