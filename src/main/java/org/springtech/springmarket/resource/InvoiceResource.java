package org.springtech.springmarket.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springtech.springmarket.domain.HttpResponse;
import org.springtech.springmarket.domain.Invoice;
import org.springtech.springmarket.dto.UserDTO;
import org.springtech.springmarket.service.CustomerService;
import org.springtech.springmarket.service.InvoiceService;
import org.springtech.springmarket.service.UserService;

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
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createInvoice(@AuthenticationPrincipal UserDTO user, @RequestBody Invoice invoice) {
        UserDTO currentUser = userService.getUserByEmail(user.getEmail());
        String agencyCode = currentUser.getAgencyCode();
        invoice.setAgencyCodeIv(agencyCode);
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
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", invoiceService.getInvoices(page.orElse(0), size.orElse(10))))
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
                                "invoice", invoice, "customer", invoiceService.getInvoice(id).getCustomer()))
                        .message("Invoice retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/addtocustomer/{id}")
    public ResponseEntity<HttpResponse> addInvoiceToCustomer(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody Invoice invoice) {
        invoiceService.addInvoiceToCustomer(id, invoice);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "customers", customerService.getCustomers()))
                        .message(String.format("Invoice added to customer with ID: %s", id))
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
