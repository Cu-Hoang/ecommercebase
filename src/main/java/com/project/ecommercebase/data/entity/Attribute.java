package com.project.ecommercebase.data.entity;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.UuidGenerator;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@ToString
public class Attribute extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    String name;

    @OneToMany(mappedBy = "value", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Value.class)
    Set<Value> values = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "attribute",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            targetEntity = ProductAttributeValue.class)
    Set<ProductAttributeValue> productAttributeValues = new LinkedHashSet<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", referencedColumnName = "id", nullable = false)
    Shop shop;

    @Override
    public String toString() {
        return "Attribute{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
