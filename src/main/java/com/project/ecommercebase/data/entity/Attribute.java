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
public class Attribute extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    String name;

    @OneToMany(mappedBy = "value", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = AttributeValue.class)
    Set<AttributeValue> attributeValues = new LinkedHashSet<>();
}
