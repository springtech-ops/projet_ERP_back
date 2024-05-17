package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.LigneCommande;
import org.springtech.springmarket.domain.Stock;

public interface LigneCommandeService {
    LigneCommande createLigneCommande(LigneCommande ligneCommande);
    Page<LigneCommande> getLigneCommandes(int page, int size);
    void addLigneCommandeToProduct(Long id, LigneCommande ligneCommande);
    void addLigneCommandeToInvoice(Long id, LigneCommande ligneCommande);
    LigneCommande getLigneCommande(Long id);
}
