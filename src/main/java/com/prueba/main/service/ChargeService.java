package com.prueba.main.service;

import com.prueba.main.entity.Charge;
import com.prueba.main.entity.Pallet;
import com.prueba.main.repository.ChargeRepository;
import com.prueba.main.repository.PalletRepository;
import com.prueba.main.util.enums.StateCharge;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChargeService {

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private PalletRepository palletRepository;

    // Get all charges
    public List<Charge> getAllCharges() {
        return chargeRepository.findAll();
    }

    // Get charge by ID
    public Optional<Charge> getChargeById(Long id) {
        return chargeRepository.findById(id);
    }

    // Create a new charge
    public Charge createCharge(Charge chargeCreateDTO) {
        try {
            return chargeRepository.save(chargeCreateDTO);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Error saving the charge: " + e.getMessage());
        }
    }

    // Get charges by pallet
    @RolesAllowed({"CARRIER", "ADMIN"})
    public List<Charge> getChargesByPallet(Long palletId) {
        return chargeRepository.findByPalletId(palletId);
    }

    // Update charge state
    @RolesAllowed({"CARRIER", "ADMIN"})
    public void updateState(Long id, String newState) {
        Charge charge = chargeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Charge not found with id: " + id));
        charge.setState(StateCharge.valueOf(newState));
        chargeRepository.save(charge);
    }

    // Get charges assigned to a carrier
    @RolesAllowed("CARRIER")
    public List<Charge> getChargesByCarrier(Long carrierId) {
        return chargeRepository.findByCarrier_Id(carrierId);
    }

    // Assign a charge to a pallet
    @RolesAllowed({"CARRIER", "ADMIN"})
    public Charge assignChargeToPallet(Long chargeId, Long palletId) {
        Charge charge = chargeRepository.findById(chargeId)
                .orElseThrow(() -> new EntityNotFoundException("Charge not found with id: " + chargeId));
        Pallet pallet = palletRepository.findById(palletId)
                .orElseThrow(() -> new EntityNotFoundException("Pallet not found with id: " + palletId));

        charge.setPallet(pallet);
        return chargeRepository.save(charge);
    }

    // Report damage to a charge
    @RolesAllowed({"CARRIER", "ADMIN"})
    public Charge reportDamage(Long chargeId) {
        Charge charge = chargeRepository.findById(chargeId)
                .orElseThrow(() -> new EntityNotFoundException("Charge not found with id: " + chargeId));

        charge.setState(StateCharge.DAMAGED); // Assume DAMAGED is a valid state
        return chargeRepository.save(charge);
    }

    // Delete a charge
    public void deleteCharge(Long id) {
        if (chargeRepository.existsById(id)) {
            chargeRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Charge not found with id: " + id);
        }
    }
}
