package org.springtech.springmarket.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springtech.springmarket.domain.HttpResponse;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.domain.Product;
import org.springtech.springmarket.dto.UserDTO;
import org.springtech.springmarket.service.*;

import java.net.URI;
import java.util.Optional;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/invoice")
@RequiredArgsConstructor
public class InvoiceResource {
    private final InvoiceService invoiceService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final AgencyService agencyService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createInvoice(@AuthenticationPrincipal UserDTO user, @RequestBody Invoice invoice) {
        UserDTO currentUser = userService.getUserByEmail(user.getEmail());
//        String agency = agencyService.findAgencyNameByCode(user.getAgencyCode());
//        invoice.setAgencyFact(agency);
//        invoice.setUserFac(user.getFirstName());
        Invoice createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", currentUser,
                                        "invoice", createdInvoice))
                                .message("Invoice created")
                                .status(CREATED)
                                .statusCode(CREATED.value())
                                .build());
    }


    @GetMapping("/new")
    public ResponseEntity<HttpResponse> newInvoice(@AuthenticationPrincipal UserDTO user) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.getCustomers()))
                        .message("Customers retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getInvoices(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        String agencyCode = user.getAgencyCode();
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", invoiceService.getInvoices(agencyCode, page.orElse(0), size.orElse(10))))
                        .message("Invoice retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getInvoice(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        Invoice invoice = invoiceService.getInvoice(id);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", invoice,
                                "customer", invoiceService.getInvoice(id).getCustomer()))
                        .message("Invoice retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/addtocustomer/{id}")
    public ResponseEntity<HttpResponse> addInvoiceToCustomer(
            @AuthenticationPrincipal UserDTO user,
            @PathVariable("id") Long id, @RequestBody Invoice invoice) {
        invoice.setAgencyFact(agencyService.findAgencyNameByCode(user.getAgencyCode()));
        invoice.setAgencyCode(user.getAgencyCode());
        invoice.setUserFac(user.getFirstName() + " " + user.getLastName());
        Invoice addedInvoice = invoiceService.addInvoiceToCustomer(id, invoice);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", addedInvoice,
                                "selectedCustomer", addedInvoice.getCustomer(),
                                "products", productService.getProductsCodeAndStatus(user.getAgencyCode())))
                        .message(String.format("Invoice added to customer %s", customerService.getCustomer(id).getName()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteInvoice(@PathVariable Long id, @AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        invoiceService.deleteInvoice(id);
        String agencyCode = user.getAgencyCode();
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", invoiceService.getInvoices(agencyCode, page.orElse(0), size.orElse(10))))
                        .message("Invoice deleted successfully By Id" + id)
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchInvoice(@AuthenticationPrincipal UserDTO user,
                                                      @RequestParam Optional<String> invoiceNumber,
                                                      @RequestParam Optional<Integer> page,
                                                      @RequestParam Optional<Integer> size) {
        String agencyCode = user.getAgencyCode();
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", invoiceService.searchInvoices(invoiceNumber.orElse(""), agencyCode, page.orElse(0), size.orElse(10))))
                        .message("Invoices retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update")
    public ResponseEntity<HttpResponse> updateInvoice(@AuthenticationPrincipal UserDTO user,
                                                      @RequestBody Invoice updateRequest) {
        Invoice invoice = invoiceService.updateInvoice(updateRequest);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "invoice", invoice,
                                "customer", invoice.getCustomer()))
                        .message("Invoice status updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
