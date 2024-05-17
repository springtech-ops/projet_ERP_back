package org.springtech.springmarket.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springtech.springmarket.domain.HttpResponse;
import org.springtech.springmarket.domain.LigneCommande;
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
        String agencyCode = currentUser.getAgencyCode();
        ligneCommande.setAgencyCodeLC(agencyCode);
        LigneCommande createdLigneCommande = ligneCommandeService.createLigneCommande(ligneCommande);
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", currentUser,
                                        "LigneCommande", createdLigneCommande))
                                .message("Ligne de commande créée")
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

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getLigneCommandes(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", ligneCommandeService.getLigneCommandes(page.orElse(0), size.orElse(10))))
                        .message("Command Line retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getLigneCommande(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        LigneCommande ligneCommande = ligneCommandeService.getLigneCommande(id);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "LigneCommande", ligneCommande, "product", ligneCommandeService.getLigneCommande(id).getProduct()))
                        .message("Command Line retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/addtoproduct/{id}")
    public ResponseEntity<HttpResponse> addLigneCommandeToProduct(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody LigneCommande ligneCommande) {
        ligneCommandeService.addLigneCommandeToProduct(id, ligneCommande);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "products", productService.getProducts()))
                        .message(String.format("Command Line added to Product with ID: %s", id))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/addtoinvoice/{id}")
    public ResponseEntity<HttpResponse> addLigneCommandeToInvoice(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody LigneCommande ligneCommande) {
        ligneCommandeService.addLigneCommandeToInvoice(id, ligneCommande);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoices", invoiceService.getInvoices()))
                        .message(String.format("Command Line added to Invoice with ID: %s", id))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


}
