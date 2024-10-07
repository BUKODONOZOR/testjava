package com.prueba.main.entity;

import com.prueba.main.util.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Carrier extends Audit {

    @NotNull
    private String name;

    @NotNull
    private String username;

    @NotNull
    private String password;


    @ManyToMany
    @JoinTable(
            name = "pallet_carrier",
            joinColumns = @JoinColumn(name = "carrier_id"),
            inverseJoinColumns = @JoinColumn(name = "pallet_id")
    )
    private List<Pallet> pallets;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

}
