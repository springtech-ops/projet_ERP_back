package org.springtech.springmarket.resource;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springtech.springmarket.domain.Agency;
import org.springtech.springmarket.domain.HttpResponse;
import org.springtech.springmarket.dto.UserDTO;
import org.springtech.springmarket.service.AgencyService;
import org.springtech.springmarket.service.UserService;

import java.net.URI;
import java.util.Optional;

import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(path = "/agency")
@RequiredArgsConstructor
public class AgencyResource {
    private AgencyService agencyService;
    private final UserService userService;
    @Autowired
    public AgencyResource(AgencyService agencyService, UserService userService) {
        this.agencyService = agencyService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getAgencies(@AuthenticationPrincipal UserDTO user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", agencyService.getAgencies(page.orElse(0), size.orElse(10))))
                        .message("Agencies retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createAgency(@AuthenticationPrincipal UserDTO user, @RequestBody Agency agency) {
        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(of("user", userService.getUserByEmail(user.getEmail()),
                                        "agency", agencyService.createAgency(agency)))
                                .message("Agency created")
                                .status(CREATED)
                                .statusCode(CREATED.value())
                                .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getAgency(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "agency", agencyService.getAgency(id)))
                        .message("Agency retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/search")
    public ResponseEntity<HttpResponse> searchAgency(@AuthenticationPrincipal UserDTO user, Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "page", agencyService.searchAgencies(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Agencies retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateAgency(@AuthenticationPrincipal UserDTO user, @RequestBody Agency agency) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserByEmail(user.getEmail()),
                                "agency", agencyService.updateAgency(agency)))
                        .message("Agency updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteAgency(@PathVariable Long id) {
        try {
            agencyService.deleteAgency(id);
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Agency deleted successfully.")
                            .status(OK)
                            .statusCode(OK.value())
                            .build());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Agency not found with id: " + id)
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("Cannot delete Agency with associated Product")
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpResponse.builder()
                            .timeStamp(now().toString())
                            .message("An error occurred while deleting Customer.")
                            .status(INTERNAL_SERVER_ERROR)
                            .statusCode(INTERNAL_SERVER_ERROR.value())
                            .build());
        }
    }


}
