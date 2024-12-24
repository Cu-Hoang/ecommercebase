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
public class Shop extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    String name;

    @OneToOne(mappedBy = "shop")
    User user;
}
