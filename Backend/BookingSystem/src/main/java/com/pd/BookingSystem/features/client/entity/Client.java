package com.pd.BookingSystem.features.client.entity;

import com.pd.BookingSystem.features.client.enums.TypeEnum;
import com.pd.BookingSystem.features.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="clients")
@DiscriminatorValue("CLIENT")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Client extends User {
    @Column(name="name")
    String name;

    @Column(name="phone")
    String phone;

    @Column(name="address")
    String address;

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    TypeEnum type;


}
