package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Fournisseur;

public interface FournisseurService {
    // Fournisseur functions
    Fournisseur createFournisseur(Fournisseur fournisseur);
    Fournisseur updateFournisseur(Fournisseur fournisseur);
    Page<Fournisseur> getFournisseurs(int page, int size);
    Iterable<Fournisseur> getFournisseurs();
    Fournisseur getFournisseur(Long id);
    Page<Fournisseur> searchFournisseurs(String name, int page, int size);
    void deleteFournisseur(Long id);
}
