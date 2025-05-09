package com.project.ecommercebase.data.entity;

import java.util.UUID;

import jakarta.persistence.*;

import org.hibernate.annotations.UuidGenerator;

import com.project.ecommercebase.enums.AddressType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class Address extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    String district;

    String province;

    String ward;

    String address_line;

    String postal_code;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    AddressType addressType = AddressType.HOME;

    Boolean isDefault;

    @Builder.Default
    Boolean isPickup = false;

    @Builder.Default
    Boolean isReturn = false;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    User user;
}
