package org.springtech.springmarket.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Customer;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.Product;
import org.springtech.springmarket.handler.AgencyNotFoundException;
import org.springtech.springmarket.handler.ResourceNotFoundException;
import org.springtech.springmarket.repository.CustomerRepository;
import org.springtech.springmarket.repository.InvoiceRepository;
import org.springtech.springmarket.service.InvoiceService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceNumber(generateReference());
        invoice.setCreatedAt(LocalDateTime.now());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Page<Invoice> getInvoices( String agencyCode, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (agencyCode == null || agencyCode.isEmpty()) {
            return invoiceRepository.findAll(of(page, size, sort));
        } else {
                return invoiceRepository.findByAgencyCode(agencyCode,of(page, size, sort));
        }
    }

    private String generateReference() {
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000); // Génère un nombre entre 1000 et 9999
        String year = new SimpleDateFormat("yyyy").format(new Date());
        return "INV-" + randomNumber + "-" + year;
    }
    @Override
    public Invoice addInvoiceToCustomer(Long id, Invoice invoice) {
        invoice.setInvoiceNumber(generateReference());
        invoice.setTotal(0);
        invoice.setCreatedAt(LocalDateTime.now());
        Customer customer = customerRepository.findById(id).get();
        invoice.setCustomer(customer);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return savedInvoice;
    }

    @Override
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id).get();
    }

    @Override
    public Iterable<Invoice> getInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public Page<Invoice> searchInvoices(String invoiceNumber, String agencyCode, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (agencyCode == null || agencyCode.isEmpty()) {
            return invoiceRepository.findByInvoiceNumberContaining(invoiceNumber, of(page, size, sort));
        } else {
                return invoiceRepository.findByInvoiceNumberContainingAndAgencyCode(invoiceNumber, agencyCode, of(page, size, sort));
        }
    }

    @Override
    public Invoice updateInvoice(Invoice updateRequest) {
        Invoice existingInvoice = invoiceRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (updateRequest.getStatus() != null) existingInvoice.setStatus(updateRequest.getStatus());

        return invoiceRepository.save(existingInvoice);
    }

}
