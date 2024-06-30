package org.springtech.springmarket.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springtech.springmarket.domain.Product;
import org.springtech.springmarket.domain.Stock;
import org.springtech.springmarket.enumeration.ProductStatus;
import org.springtech.springmarket.enumeration.StockType;
import org.springtech.springmarket.repository.ProductRepository;
import org.springtech.springmarket.repository.StockRepository;
import org.springtech.springmarket.service.StockService;

import java.util.Date;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final NamedParameterJdbcTemplate jdbc;
    @Override
    public Stock createStock(Stock stock) {
        setStockDetails(stock);
        return stockRepository.save(stock);
    }

    @Override
    public Page<Stock> getStocks(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return stockRepository.findAll(of(page, size, sort));
    }

    @Override
    public Page<Stock> searchStocks(String stockNumber, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return stockRepository.findByStockNumberContaining(stockNumber, of(page, size, sort));
    }

    @Override
    public void addStockToProduct(Long id, Stock stock) {
        setStockDetails(stock);
        Product product = productRepository.findById(id).get(); //.orElseThrow(() -> new EntityNotFoundException("Agency not found with id: " + id));
        if (stock.getAction() == StockType.STOCK_IN) {
            product.setQuantity(product.getQuantity() + stock.getQuantityIn());
            product.setPrixAchat(stock.getPrixAchat());
            product.setPrixVente(stock.getPrixVente());
            product.setUpdateAt(new Date());
        } else if (stock.getAction() == StockType.STOCK_OUT) {
            if (product.getQuantity() >= stock.getQuantityOut()) {
                product.setQuantity(product.getQuantity() - stock.getQuantityOut());
                stock.setPrixAchat(product.getPrixAchat());
                stock.setPrixVente(product.getPrixVente());
                stock.setQuantityOut(stock.getQuantityOut());
                product.setUpdateAt(new Date());
            } else {
                throw new IllegalArgumentException("The quantity of product present is insufficient.");
            }
        }

        // Mettre à jour le statut du produit en fonction de sa quantité
        if (product.getQuantity() == 0) {
            product.setStatus(String.valueOf(ProductStatus.EMPTY));
            product.setIsActive(false);
        } else if (product.getQuantity() <= 5) {
            product.setStatus(String.valueOf(ProductStatus.WARNING));
            product.setIsActive(true); // Cela peut être omis si isActive est déjà true
        } else {
            product.setStatus(String.valueOf(ProductStatus.AVAILABLE));
            product.setIsActive(true); // Cela peut être omis si isActive est déjà true
        }

        stock.setCreatedAt(new Date());
        stock.setProduct(product);
        stockRepository.save(stock);
    }

    @Override
    public Stock getStock(Long id) {
        return stockRepository.findById(id).get();
    }

    public void setStockDetails(Stock stock) {
        stock.setStockNumber(randomAlphanumeric(8).toUpperCase());
    }

    @Override
    public void deleteStock(Long id) {
        // Vérifie d'abord si le fournisseur existe
        if (!stockRepository.existsById(id)) {
            throw new EntityNotFoundException("Fournisseur not found with id: %S" + id);
        }
        // Supprime le fournisseur
        stockRepository.deleteById(id);
    }
}
