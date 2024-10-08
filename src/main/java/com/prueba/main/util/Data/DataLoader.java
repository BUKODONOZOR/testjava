package com.prueba.main.util.Data;


import com.prueba.main.entity.Carrier;
import com.prueba.main.entity.Charge;
import com.prueba.main.entity.Pallet;
import com.prueba.main.repository.CarrierRepository;
import com.prueba.main.repository.ChargeRepository;
import com.prueba.main.repository.PalletRepository;
import com.prueba.main.util.enums.Role;
import com.prueba.main.util.enums.StateCharge;
import com.prueba.main.util.enums.StatePallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private PalletRepository palletRepository;

    @Autowired
    private ChargeRepository chargeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Seed carriers (one per role)
        Carrier carrierAdmin = new Carrier();
        carrierAdmin.setName("Admin Carrier");
        carrierAdmin.setUsername("admin_carrier");
        carrierAdmin.setPassword("admin123");
        carrierAdmin.setRole(Role.ADMIN);

        Carrier carrierOperator = new Carrier();
        carrierOperator.setName("Operator Carrier");
        carrierOperator.setUsername("operator_carrier");
        carrierOperator.setPassword("operator123");
        carrierOperator.setRole(Role.OPERATOR);

        Carrier carrierTransporter = new Carrier();
        carrierTransporter.setName("Transporter Carrier");
        carrierTransporter.setUsername("transporter_carrier");
        carrierTransporter.setPassword("transporter123");
        carrierTransporter.setRole(Role.ADMIN);

        carrierRepository.saveAll(Arrays.asList(carrierAdmin, carrierOperator, carrierTransporter));

        // Seed pallets (5 pallets)
        for (int i = 1; i <= 5; i++) {
            Pallet pallet = new Pallet();
            pallet.setCapacityMaximum(1000.0 + i * 100);
            pallet.setStatePallet(StatePallet.AVAILABLE);
            pallet.setLocation("Warehouse " + i);
            palletRepository.save(pallet);
        }

        // Seed charges (at least 2)
        Pallet pallet1 = palletRepository.findById(1L).orElseThrow();
        Pallet pallet2 = palletRepository.findById(2L).orElseThrow();

        Charge charge1 = new Charge();
        charge1.setWeight(200);
        charge1.setWidth(50);
        charge1.setLength(100);
        charge1.setHigh(150);
        charge1.setState(StateCharge.PENDING);
        charge1.setPallet(pallet1);

        Charge charge2 = new Charge();
        charge2.setWeight(300);
        charge2.setWidth(60);
        charge2.setLength(110);
        charge2.setHigh(160);
        charge2.setState(StateCharge.INPROGRES);
        charge2.setPallet(pallet2);

        chargeRepository.saveAll(Arrays.asList(charge1, charge2));
    }
}
