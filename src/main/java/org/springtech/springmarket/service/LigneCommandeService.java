package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.LigneCommande;

public interface LigneCommandeService {
    LigneCommande createLigneCommande(LigneCommande ligneCommande);
//    Page<LigneCommande> getLigneCommandes(int page, int size);
    LigneCommande addLigneCommandeToEntities(Long invoiceId, Long productId, LigneCommande ligneCommande);
    LigneCommande getLigneCommande(Long id);

    void deleteLigneCommande(Long id);

    Page<LigneCommande> searchLigneCommandes(String name, int page, int size);
}
