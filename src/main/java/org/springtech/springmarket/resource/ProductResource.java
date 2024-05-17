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
                        .message("Users retrieved")
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
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "product", productService.getProduct(id)))
                        .message("Product retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

//    @GetMapping("/search")
//    public ResponseEntity<HttpResponse> searchProduct(@AuthenticationPrincipal UserDTO user, Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
//        return ResponseEntity.ok(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .data(of("user", userService.getUserByEmail(user.getEmail()),
//                                "page", productService.searchProducts(name.orElse(""), page.orElse(0), size.orElse(10))))
//                        .message("Products retrieved")
//                        .status(OK)
//                        .statusCode(OK.value())
//                        .build());
//    }


//    @PostMapping("/addagencytoproduct/{id}/")
//    public ResponseEntity<HttpResponse> addAgencyToProduct(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Product product) {
//        agencyService.addAgencyToProduct(id, product);
//        return ResponseEntity.ok(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .data(of("user", userService.getUserByEmail(user.getEmail()),
//                                "agencies", agencyService.getAgencies()))
//                        .message(String.format("Agency Added to Product with ID: %s", id))
//                        .status(OK)
//                        .statusCode(OK.value())
//                        .build());
//    }

    @PostMapping("/addtocategory/{id}")
    public ResponseEntity<HttpResponse> addInvoiceToCustomer(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Product product) {
        productService.addProductToCategory(id, product);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "categories", categoryService.getCategories()))
                        .message(String.format("Product added to category with ID: %s", id))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/addtofournisseur/{id}")
    public ResponseEntity<HttpResponse> addProductToFournisseur(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Product product) {
        productService.addProductToFournisseur(id, product);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "fournisseurs", fournisseurService.getFournisseurs()))
                        .message(String.format("Product added to fournisseur with ID: %s", id))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/addtoagency/{id}")
    public ResponseEntity<HttpResponse> addProductToAgency(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Product product) {
        productService.addProductToAgency(id, product);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "Agencies", agencyService.getAgencies()))
                        .message(String.format("Product added to Agency with ID: %s", id))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

//    @PostMapping("/addcategorytoproduct/{id}")   /// A implementer
//    public ResponseEntity<HttpResponse> addCategoryToProduct(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Product product) {
//        categoryService.addCategoryToProduct(id, product);
//        return ResponseEntity.ok(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .data(of("user", userService.getUserByEmail(user.getEmail()),
//                                "categories", agencyService.getAgencies()))
//                        .message(String.format("Category Added to Product with ID: %s", id))
//                        .status(OK)
//                        .statusCode(OK.value())
//                        .build());
//    }


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
    public ResponseEntity<HttpResponse> updateProduct(@AuthenticationPrincipal UserDTO user, @RequestBody Product product) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "product", productService.updateProduct(product)))
                        .message("Product updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/new")
    public ResponseEntity<HttpResponse> newCategory(@AuthenticationPrincipal UserDTO user) {
        try {
            Map<String, Object> responseData = Map.of(
                    "user", userService.getUserByEmail(user.getEmail()),
                    "Categories", categoryService.getCategories(),
                    "Agencies", agencyService.getAgencies(),
                    "Fournisseurs", fournisseurService.getFournisseurs()
            );

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(responseData)
                            .message("Categories and Agencies retrieved")
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
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Product not found with id: " + id)
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
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
