package org.springtech.springmarket.resource;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springtech.springmarket.domain.Fournisseur;
import org.springtech.springmarket.domain.HttpResponse;
import org.springtech.springmarket.dto.UserDTO;
import org.springtech.springmarket.service.FournisseurService;
import org.springtech.springmarket.service.UserService;

import java.net.URI;
import java.util.Optional;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "/fournisseur")
@RequiredArgsConstructor
public class FournisseurResource {
    private FournisseurService fournisseurService;
    private final UserService userService;
    @Autowired
    public FournisseurResource(FournisseurService fournisseurService, UserService userService) {
        this.fournisseurService = fournisseurService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getFournisseurs(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", fournisseurService.getFournisseurs(page.orElse(0), size.orElse(10))))
                        .message("Fournisseurs retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createFournisseur(@AuthenticationPrincipal UserDTO user, @RequestBody Fournisseur fournisseur) {
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", userService.getUserByEmail(user.getEmail()),
                                        "fournisseur", fournisseurService.createFournisseur(fournisseur)))
                                .message("Fournisseur created")
                                .status(CREATED)
                                .statusCode(CREATED.value())
                                .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getFournisseur(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "fournisseur", fournisseurService.getFournisseur(id)))
                        .message("Fournisseur retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchFournisseur(@AuthenticationPrincipal UserDTO user, Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", fournisseurService.searchFournisseurs(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Fournisseurs retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateFournisseur(@AuthenticationPrincipal UserDTO user, @RequestBody Fournisseur fournisseur) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "fournisseur", fournisseurService.updateFournisseur(fournisseur)))
                        .message("Fournisseur updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteFournisseur(@PathVariable Long id) {
        try {
            fournisseurService.deleteFournisseur(id);
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Fournisseur deleted successfully.")
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Fournisseur not found with id: " + id)
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Cannot delete fournisseur with associated stocks")
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
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
