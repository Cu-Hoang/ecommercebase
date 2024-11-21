package com.project.ecommercebase.data.entity;

import java.time.LocalDateTime;
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
public class Code extends BaseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    String codes;

    LocalDateTime expiredAt;

    @Builder.Default
    Boolean isUsed = false;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    User user;
}
