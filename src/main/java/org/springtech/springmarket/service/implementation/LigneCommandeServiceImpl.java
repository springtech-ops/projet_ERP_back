package org.springtech.springmarket.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.LigneCommande;
import org.springtech.springmarket.domain.Product;
import org.springtech.springmarket.repository.InvoiceRepository;
import org.springtech.springmarket.repository.LigneCommandeRepository;
import org.springtech.springmarket.repository.ProductRepository;
import org.springtech.springmarket.service.LigneCommandeService;

import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LigneCommandeServiceImpl implements LigneCommandeService {

    private final LigneCommandeRepository ligneCommandeRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;
    @Override
    public LigneCommande createLigneCommande(LigneCommande ligneCommande) {
        return ligneCommandeRepository.save(ligneCommande);
    }

    @Override
    public Page<LigneCommande> getLigneCommandes(int page, int size) {
        return ligneCommandeRepository.findAll(of(page, size));
    }

    @Override
    public void addLigneCommandeToProduct(Long id, LigneCommande ligneCommande) {
        Product product = productRepository.findById(id).get();
        int newQuantity = product.getQuantity() - ligneCommande.getQuantityLC();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("La quantité de produit disponible est insuffisante.");
        }
        product.setQuantity(newQuantity);
        productRepository.save(product);
        ligneCommandeRepository.save(ligneCommande);
    }

    @Override
    public void addLigneCommandeToInvoice(Long id, LigneCommande ligneCommande) {
        Invoice invoice = invoiceRepository.findById(id).get();
        ligneCommande.setInvoice(invoice);
        ligneCommandeRepository.save(ligneCommande);
    }

    @Override
    public LigneCommande getLigneCommande(Long id) {
        return ligneCommandeRepository.findById(id).get();
    }
}
