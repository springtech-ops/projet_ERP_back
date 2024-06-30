package org.springtech.springmarket.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.*;
import org.springtech.springmarket.enumeration.ProductStatus;
import org.springtech.springmarket.handler.InsufficientQuantityException;
import org.springtech.springmarket.handler.ResourceNotFoundException;
import org.springtech.springmarket.repository.InvoiceRepository;
import org.springtech.springmarket.repository.LigneCommandeRepository;
import org.springtech.springmarket.repository.ProductRepository;
import org.springtech.springmarket.service.LigneCommandeService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

//    @Override
//    public Page<LigneCommande> getLigneCommandes(int page, int size) {
//        return ligneCommandeRepository.findAll(of(page, size));
//    }

    @Override
    public LigneCommande addLigneCommandeToEntities(Long invoiceId, Long productId, LigneCommande ligneCommande) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + invoiceId));
        ligneCommande.setInvoice(invoice);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        ligneCommande.setProduct(product);

        int newQuantity = product.getQuantity() - ligneCommande.getQuantityLC();
        if (newQuantity < 0) {
//            throw new InsufficientQuantityException("Available Product Quantity is insufficient.");
        } else {
            product.setQuantity(newQuantity);
            ligneCommande.setName(product.getName());
            ligneCommande.setPrixAchat(product.getPrixAchat());
            ligneCommande.setPrixVente(product.getPrixVente());


            ligneCommande.setReference(product.getCode());

            ligneCommande.setPrixVenteTotal(ligneCommande.getPrixVente() * ligneCommande.getQuantityLC());
            ligneCommande.setPrixAchatTotal(ligneCommande.getPrixAchat() * ligneCommande.getQuantityLC());
            ligneCommande.setBeneficeTotal(ligneCommande.getPrixVenteTotal() - ligneCommande.getPrixAchatTotal());

            ligneCommande.setCreatedAt(new Date());

            invoice.setTotal(invoice.getTotal()+ligneCommande.getPrixVenteTotal());

            if (newQuantity < 5) {
                product.setStatus(String.valueOf(ProductStatus.WARNING));
            }
            if (newQuantity == 0) {
                product.setStatus(String.valueOf(ProductStatus.EMPTY));
                product.setIsActive(false);
            }
            productRepository.save(product);
            ligneCommandeRepository.save(ligneCommande);
            invoiceRepository.save(invoice);
        }
        return ligneCommande;
    }



    @Override
    public LigneCommande getLigneCommande(Long id) {
        return ligneCommandeRepository.findById(id).get();
    }

    @Override
    public void deleteLigneCommande(Long id) {
        LigneCommande ligneCommande = ligneCommandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LigneCommande not found with id: " + id));

        Product product = ligneCommande.getProduct();
        product.setQuantity(product.getQuantity() + ligneCommande.getQuantityLC());

        // Mettre à jour le statut du produit en fonction de la nouvelle quantité
        if (product.getQuantity() == 0) {
            product.setStatus(String.valueOf(ProductStatus.EMPTY));
            product.setIsActive(false);
        } else if (product.getQuantity() > 0 && product.getQuantity() <= 5) {
            product.setStatus(String.valueOf(ProductStatus.WARNING));
            product.setIsActive(true);  // S'assurer que le produit est actif s'il est en alerte
        } else {
            product.setStatus(String.valueOf(ProductStatus.AVAILABLE));
            product.setIsActive(true);  // S'assurer que le produit est actif s'il est disponible
        }

        productRepository.save(product);

        // Mettre à jour la facture
        Invoice invoice = ligneCommande.getInvoice();
        invoice.setTotal(invoice.getTotal() - ligneCommande.getPrixVenteTotal());
        invoiceRepository.save(invoice);

        ligneCommandeRepository.deleteById(id);
    }

    @Override
    public Page<LigneCommande> searchLigneCommandes(String name, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
            return ligneCommandeRepository.findByNameContaining(name, of(page, size, sort));

    }

}
