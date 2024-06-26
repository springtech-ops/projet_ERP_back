package org.springtech.springmarket.service;

import org.springframework.data.domain.Page;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.Stock;

public interface StockService {
    // Invoice functions
    Stock createStock(Stock stock);
    Page<Stock> getStocks(int page, int size);
    void addStockToProduct(Long id, Stock stock);
    Stock getStock(Long id);
    void deleteStock(Long id);
    Page<Stock> searchStocks(String stockNumber, int page, int size);
}
