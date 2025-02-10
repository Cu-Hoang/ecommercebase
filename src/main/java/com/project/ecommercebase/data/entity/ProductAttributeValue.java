package com.project.ecommercebase.data.entity;

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
public class ProductAttributeValue extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, columnDefinition = "uuid")
    Product product;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id", nullable = false, columnDefinition = "uuid")
    Attribute attribute;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "value_id", referencedColumnName = "id", nullable = false, columnDefinition = "uuid")
    Value value;
}
