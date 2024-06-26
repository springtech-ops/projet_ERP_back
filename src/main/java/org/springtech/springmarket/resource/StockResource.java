package org.springtech.springmarket.resource;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springtech.springmarket.domain.HttpResponse;
import org.springtech.springmarket.domain.Stock;
import org.springtech.springmarket.dto.UserDTO;
import org.springtech.springmarket.service.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(path = "/stock")
@RequiredArgsConstructor
public class StockResource {

    private ProductService productService;
    private StockService stockService;
    private final UserService userService;

    @Autowired
    public StockResource(ProductService productService, StockService stockService, UserService userService, FournisseurService fournisseurService) {
        this.stockService = stockService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createStock(@AuthenticationPrincipal UserDTO user, @RequestBody Stock stock) {
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", userService.getUserByEmail(user.getEmail()),
                                        "stock", stockService.createStock(stock)))
                                .message("Stock created")
                                .status(CREATED)
                                .statusCode(CREATED.value())
                                .build());
    }

    @GetMapping("/new")
    public ResponseEntity<HttpResponse> newStock(@AuthenticationPrincipal UserDTO user) {
        try {
            String agencyCode = user.getAgencyCode();
            Map<String, Object> responseData;
            if (agencyCode == null || agencyCode.isEmpty()) {
                responseData = Map.of(
                        "user", userService.getUserByEmail(user.getEmail()),
                        "products", productService.getProducts()
                );
            } else {
                responseData = Map.of(
                        "user", userService.getUserByEmail(user.getEmail()),
                        "products", productService.getProducts(agencyCode)
                );
            }

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(responseData)
                            .message("Products retrieved")
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("An error occurred while retrieving data.")
                            .status(INTERNAL_SERVER_ERROR)
                            .statusCode(INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }


    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getStocks(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", stockService.getStocks(page.orElse(0), size.orElse(10))))
                        .message("Stocks retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchStock(@AuthenticationPrincipal UserDTO user, Optional<String> stockNumber, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", stockService.searchStocks(stockNumber.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Stocks retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getStock(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        Stock stock = stockService.getStock(id);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "stock", stock,
                                "product", stock.getProduct(),
                                "agency", stock.getProduct().getAgency()))
                        .message("Stock retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


    @PostMapping("/addtoproduct/{id}")
    public ResponseEntity<HttpResponse> addStockToProduct(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Stock stock) {
        stockService.addStockToProduct(id, stock);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "products", productService.getProducts()))
                        .message(String.format("Stock added to Product with ID: %s", id))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteStock(@PathVariable Long id) {
        try {
            stockService.deleteStock(id);
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Stock deleted successfully.")
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Stock not found with id: %s" + id)
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("An error occurred while deleting fournisseur.")
                            .status(INTERNAL_SERVER_ERROR)
                            .statusCode(INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }
}
