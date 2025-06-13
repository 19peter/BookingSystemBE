package com.pd.BookingSystem.features.language.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "interpreter_language_price_lk")
public class InterpreterLanguagePriceLK {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @JoinColumn(name = "language_id", referencedColumnName = "id")
    @OneToOne
    Language language;

    @Column
    Double pricePerHour;

}
