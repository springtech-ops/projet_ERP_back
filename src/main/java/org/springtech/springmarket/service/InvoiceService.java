package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.Product;

public interface InvoiceService {

    // Invoice functions
    Invoice createInvoice(Invoice invoice);
//    Page<Invoice> getInvoices(int page, int size);
    Invoice addInvoiceToCustomer(Long id, Invoice invoice);
    Invoice getInvoice(Long id);
    Iterable<Invoice> getInvoices();
    void deleteInvoice(Long id);
    Page<Invoice> searchInvoices(String invoiceNumber, String agencyCode, int page, int size);
//    Page<Invoice> searchInvoices(String invoiceNumber, int page, int size);

    Page<Invoice> getInvoices(String agencyCode, int page, int size);

    Invoice updateInvoice(Invoice invoice);
}
