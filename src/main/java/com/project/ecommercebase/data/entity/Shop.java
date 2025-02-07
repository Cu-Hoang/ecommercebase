package com.project.ecommercebase.data.entity;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.UuidGenerator;

import com.project.ecommercebase.enums.ShopTag;
import com.project.ecommercebase.enums.Status;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Shop extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    String name;

    String province;

    @Enumerated(EnumType.STRING)
    ShopTag shopTag;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @OneToOne(mappedBy = "shop")
    User user;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Product.class)
    Set<Product> products = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Category.class)
    Set<Category> categories = new LinkedHashSet<>();
}
