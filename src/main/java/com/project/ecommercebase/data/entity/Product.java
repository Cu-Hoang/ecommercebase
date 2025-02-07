package com.project.ecommercebase.data.entity;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
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
public class Product extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    String description;

    String name;

    BigDecimal price;

    Integer quantity;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    Visibility visibility = Visibility.ON;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    String image;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Image.class)
    Set<Image> images = new LinkedHashSet<>();

    @OneToMany(mappedBy = "value", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AttributeValue.class)
    Set<AttributeValue> attributeValues = new LinkedHashSet<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    Category category;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", referencedColumnName = "id", nullable = false)
    Shop shop;
}
