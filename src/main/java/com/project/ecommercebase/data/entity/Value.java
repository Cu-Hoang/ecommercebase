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
public class Value extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id", nullable = false)
    Attribute attribute;

    @OneToMany(
            mappedBy = "value",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            targetEntity = ProductAttributeValue.class)
    Set<ProductAttributeValue> productAttributeValues = new LinkedHashSet<>();

    String value;
}
