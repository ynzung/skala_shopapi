package com.sk.skala.shopapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "favorites",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"customer_id", "product_id"}
        )
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder.Default
    @Column(nullable = false)
    private boolean favorite = true;

    public void toggle() {
        this.favorite = !this.favorite;
    }
}
