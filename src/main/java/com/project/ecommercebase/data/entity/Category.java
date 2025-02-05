package com.project.ecommercebase.data.entity;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.UuidGenerator;

import com.project.ecommercebase.enums.Status;
import com.project.ecommercebase.enums.Visibility;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class Category extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    String name;

    UUID parent_id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    Visibility visibility = Visibility.ON;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Product.class)
    Set<Product> products;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", referencedColumnName = "id", nullable = false)
    Shop shop;
}
