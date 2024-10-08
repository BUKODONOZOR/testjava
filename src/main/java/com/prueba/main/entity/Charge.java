package com.prueba.main.entity;

import com.prueba.main.util.enums.StateCharge;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Charge extends Audit {

    @NotNull
    private double weight;

    @NotNull
    private double width;

    @NotNull
    private double length;

    @NotNull
    private double high;

    @Enumerated(EnumType.STRING)
    private StateCharge state;

    @ManyToOne
    @JoinColumn(name = "pallet_id")
    private Pallet pallet;

    // Relación con Carrier para asignar un transportista a la carga
    @ManyToOne
    @JoinColumn(name = "carrier_id") // Esto crea el campo carrier_id en la tabla Charge
    private Carrier carrier;
}
