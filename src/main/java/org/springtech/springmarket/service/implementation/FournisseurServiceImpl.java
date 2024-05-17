package org.springtech.springmarket.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Fournisseur;
import org.springtech.springmarket.repository.AgencyRepository;
import org.springtech.springmarket.repository.FournisseurRepository;
import org.springtech.springmarket.repository.ProductRepository;
import org.springtech.springmarket.repository.StockRepository;
import org.springtech.springmarket.service.FournisseurService;

import java.util.Date;

import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FournisseurServiceImpl implements FournisseurService {
    private final FournisseurRepository fournisseurRepository;
    private final ProductRepository productRepository;
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Fournisseur createFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public Fournisseur updateFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public Page<Fournisseur> getFournisseurs(int page, int size) {
        return fournisseurRepository.findAll(of(page, size));
    }

    @Override
    public Iterable<Fournisseur> getFournisseurs() {
        return fournisseurRepository.findAll();
    }

    @Override
    public Fournisseur getFournisseur(Long id) {
        return fournisseurRepository.findById(id).get();
    }

    @Override
    public Page<Fournisseur> searchFournisseurs(String name, int page, int size) {
        return fournisseurRepository.findByNameContaining(name, of(page, size));
    }

    @Override
    public void deleteFournisseur(Long id) {
            // Vérifie d'abord si le fournisseur existe
            if (!fournisseurRepository.existsById(id)) {
                throw new EntityNotFoundException("Fournisseur not found with id: " + id);
            }
            // Vérifie si des stocks sont associés à ce fournisseur
            long count = productRepository.countByFournisseurId(id);
            if (count > 0) {
                throw new IllegalStateException("Cannot delete fournisseur with associated product(s)");
            }
            // Supprime le fournisseur
            fournisseurRepository.deleteById(id);
    }

}
