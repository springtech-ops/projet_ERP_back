package org.springtech.springmarket.resource;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springtech.springmarket.domain.HttpResponse;
import org.springtech.springmarket.domain.LigneCommande;
import org.springtech.springmarket.domain.Product;
import org.springtech.springmarket.dto.UserDTO;
import org.springtech.springmarket.service.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "/ligne")
@RequiredArgsConstructor
public class LigneCommandeResource {
    private final LigneCommandeService ligneCommandeService;
    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public LigneCommandeResource(ProductService productService, InvoiceService invoiceService, LigneCommandeService ligneCommandeService, UserService userService, FournisseurService fournisseurService) {
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.ligneCommandeService = ligneCommandeService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createLigneCommande(@AuthenticationPrincipal UserDTO user, @RequestBody LigneCommande ligneCommande) {
        UserDTO currentUser = userService.getUserByEmail(user.getEmail());
//        String agencyCode = currentUser.getAgencyCode();
        ligneCommande.setAgencyCodeLC(user.getAgencyCode());
        LigneCommande createdLigneCommande = ligneCommandeService.createLigneCommande(ligneCommande);
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", currentUser,
                                        "ligneCommande", createdLigneCommande,
                                        "invoice", createdLigneCommande.getInvoice(),
                                        "products", productService.getProductsCodeAndStatus(user.getAgencyCode()),
                                        "selectedCustomer", createdLigneCommande.getInvoice().getCustomer()))
                                .message(String.format("New Order Added with ID: %s", createdLigneCommande.getId()))
                                .status(CREATED)
                                .statusCode(CREATED.value())
                                .build());
    }


    @GetMapping("/new")
    public ResponseEntity<HttpResponse> newLigneCommande(@AuthenticationPrincipal UserDTO user) {
        try {
            String agencyCode = user.getAgencyCode();
            Map<String, Object> responseData;
            if (agencyCode == null || agencyCode.isEmpty()) {
                responseData = Map.of(
                        "user", userService.getUserByEmail(user.getEmail()),
                        "Products", productService.getProducts(),
                        "invoices", invoiceService.getInvoices()
                );
            } else {
                responseData = Map.of(
                        "user", userService.getUserByEmail(user.getEmail()),
                        "Products", productService.getProducts(agencyCode),
                        "invoices", invoiceService.getInvoices()
                );
            }

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(responseData)
                            .message("Products and Invoices retrieved")
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

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchLigne(@AuthenticationPrincipal UserDTO user, Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", ligneCommandeService.searchLigneCommandes(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Orders retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getLigneCommande(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("id") Long id) {
        LigneCommande ligneCommande = ligneCommandeService.getLigneCommande(id);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "ligneCommande", ligneCommande,
                                "invoice", ligneCommande.getInvoice(),
                                "customer", ligneCommande.getInvoice().getCustomer(),
                                "product", ligneCommande.getProduct(),
                                "agency", ligneCommande.getProduct().getAgency()))
                        .message("Oder retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/addto/{invoiceId}/{productId}")
    public ResponseEntity<HttpResponse> assignProductToEntities(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("invoiceId") Long invoiceId,
            @PathVariable("productId") Long productId,
            @RequestBody LigneCommande ligneCommande)
    {
        LigneCommande createdLigneCommande = ligneCommandeService.addLigneCommandeToEntities(invoiceId, productId, ligneCommande);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", invoiceService.getInvoice(invoiceId),
                                "products", productService.getProductsCodeAndStatus(user.getAgencyCode()),
                                "selectedCustomer", createdLigneCommande.getInvoice().getCustomer()))
                        .message(String.format("%s was added", createdLigneCommande.getName()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteLigneCommande(@AuthenticationPrincipal UserDTO user, @PathVariable Long id) {
        try {
            LigneCommande ligneCommande = ligneCommandeService.getLigneCommande(id);
            ligneCommandeService.deleteLigneCommande(id);
            return ResponseEntity.ok(
                    HttpResponse.builder()
                .timeStamp(now().toString())
                .data(of("user", userService.getUserByEmail(user.getEmail()),
                        "invoice", ligneCommande.getInvoice(),
                        "products", productService.getProductsCodeAndStatus(user.getAgencyCode()),
                        "selectedCustomer", ligneCommande.getInvoice().getCustomer()))
                .message(String.format("%s was deleted", ligneCommande.getName()))
                .status(OK)
                .statusCode(OK.value())
                .build());
    } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Command line not found with id: " + id)
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Cannot delete Command line with associated products")
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("An error occurred while deleting category.")
                            .status(INTERNAL_SERVER_ERROR)
                            .statusCode(INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }


    @DeleteMapping("/deleteLcDetail/{id}")
    public ResponseEntity<HttpResponse> deleteLigne(@AuthenticationPrincipal UserDTO user, @PathVariable Long id) {
            LigneCommande ligneCommande = ligneCommandeService.getLigneCommande(id);
            ligneCommandeService.deleteLigneCommande(id);
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(of("user", userService.getUserByEmail(user.getEmail()),
                                    "invoice", ligneCommande.getInvoice(),
                                    "customer", ligneCommande.getInvoice().getCustomer()))
                            .message(String.format("%s was deleted", ligneCommande.getName()))
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        }


}
