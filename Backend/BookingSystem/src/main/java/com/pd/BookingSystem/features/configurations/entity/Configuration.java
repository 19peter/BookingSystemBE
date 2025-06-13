package com.pd.BookingSystem.features.configurations.entity;

import com.pd.BookingSystem.features.configurations.enums.ConfigTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "configuration")
public class Configuration {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String key;

    @Column(nullable = false)
    String value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ConfigTypeEnum type;
}
