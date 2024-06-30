package org.springtech.springmarket.resource;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springtech.springmarket.domain.*;
import org.springtech.springmarket.dto.UserDTO;
import org.springtech.springmarket.service.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
public class ProductResource {
    private ProductService productService;
    private FournisseurService fournisseurService;
    private AgencyService agencyService;
    private CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public ProductResource(ProductService productService, FournisseurService fournisseurService, AgencyService agencyService, CategoryService categoryService, UserService userService) {
        this.productService = productService;
        this.agencyService = agencyService;
        this.fournisseurService = fournisseurService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getProducts(@AuthenticationPrincipal UserDTO user,
                                                    @RequestParam Optional<Integer> page,
                                                    @RequestParam Optional<Integer> size) {
        String agencyCode = user.getAgencyCode();
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", productService.getProducts(page.orElse(0), size.orElse(10), agencyCode)))
                        .message("Products retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createProduct(@AuthenticationPrincipal UserDTO user, @RequestBody Product product) {
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", userService.getUserByEmail(user.getEmail()),
                                        "product", productService.createProduct(product)))
                                .message("Product created")
                                .status(CREATED)
                                .statusCode(CREATED.value())
                                .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getProduct(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "product", product,
                                "fournisseur", product.getFournisseur(),
                                "agency", product.getAgency(),
                                "category", product.getCategory()))
                        .message("Product retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/addto/{agencyId}/{fournisseurId}/{categoryId}")
    public ResponseEntity<HttpResponse> assignProductToEntities(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("agencyId") Long agencyId,
            @PathVariable("fournisseurId") Long fournisseurId,
            @PathVariable("categoryId") Long categoryId,
            @RequestBody Product product
    ) {
        product.setAgencyName(agencyService.findAgencyNameById(agencyId));
        productService.addProductToEntities(agencyId, fournisseurId, categoryId, product);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail())))
                        .message("Product assigned to agency, fournisseur, and category successfully")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchProduct(@AuthenticationPrincipal UserDTO user,
                                                      @RequestParam Optional<String> name,
                                                      @RequestParam Optional<Integer> page,
                                                      @RequestParam Optional<Integer> size) {
        String agencyCode = user.getAgencyCode();
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", productService.searchProducts(name.orElse(""), agencyCode, page.orElse(0), size.orElse(10))))
                        .message("Products retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update")
    public ResponseEntity<HttpResponse> updateProduct(@AuthenticationPrincipal UserDTO user,
                                                      @RequestBody Product updateRequest) {
        Product product = productService.updateProduct(updateRequest);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "product", product,
                                "fournisseur", product.getFournisseur(),
                                "agency", product.getAgency(),
                                "category", product.getCategory()))
                        .message("Product partially updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


    @GetMapping("/new")
    public ResponseEntity<HttpResponse> newCategory(@AuthenticationPrincipal UserDTO user) {
        try {
            Map<String, Object> responseData = Map.of(
                    "user", userService.getUserByEmail(user.getEmail()),
                    "categories", categoryService.getCategories(),
                    "agencies", agencyService.getAgencies(),
                    "fournisseurs", fournisseurService.getFournisseurs()
            );

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(responseData)
                            .message("Categories, Agencies and Suppliers retrieved")
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Product deleted successfully.")
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Cannot delete Product with associated stocks")
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("An error occurred while deleting Product.")
                            .status(INTERNAL_SERVER_ERROR)
                            .statusCode(INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }

}
