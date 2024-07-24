package org.springtech.springmarket.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String description;
    private Date updateAt;
    private double prixAchat;
    private double prixVente;
    private int quantity;
    private String status;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonIgnore
    private Agency agency;

    private String agencyName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    @JsonIgnore
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "product", fetch = EAGER, cascade = ALL)
    private Collection<LigneCommande> ligneCommandes;

    @OneToMany(mappedBy = "product", fetch = EAGER, cascade = ALL)
    private Collection<Stock> stocks;

}
