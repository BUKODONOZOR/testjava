package com.prueba.main.entity;

import com.prueba.main.util.enums.StatePallet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Pallet extends Audit {

    @NotNull
    private double capacityMaximum;

    @Enumerated(EnumType.STRING)
    private StatePallet statePallet;

    @OneToMany(mappedBy = "pallet")
    private List<Charge> charges;

    private String location;

}


