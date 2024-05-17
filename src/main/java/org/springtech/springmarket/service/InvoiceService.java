package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.Product;

public interface InvoiceService {

    // Invoice functions
    Invoice createInvoice(Invoice invoice);
    Page<Invoice> getInvoices(int page, int size);
    void addInvoiceToCustomer(Long id, Invoice invoice);
    Invoice getInvoice(Long id);
    Iterable<Invoice> getInvoices();
}
