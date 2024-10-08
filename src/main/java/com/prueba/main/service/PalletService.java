package com.prueba.main.service;


import com.prueba.main.entity.Pallet;
import com.prueba.main.entity.Charge;
import com.prueba.main.repository.PalletRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PalletService {

    @Autowired
    private PalletRepository palletRepository;

    public List<Pallet> getAllPallets() {
        return palletRepository.findAll();
    }

    public Optional<Pallet> getPalletById(Long id){
        return palletRepository.findById(id);
    }

    public Pallet createPallet(Pallet palletCreateDTO) {
        try {
            return palletRepository.save(palletCreateDTO);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Error al guardar el palet: " + e.getMessage());
        }
    }


    public List<Charge> getChargesByPallet(Long palletId) {
        Pallet pallet = palletRepository.findById(palletId)
                .orElseThrow(() -> new EntityNotFoundException("Palet no encontrado con id: " + palletId));

        return pallet.getCharges();
    }


    public void deletePallet(Long id) {
        if (palletRepository.existsById(id)) {
            palletRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Palet no encontrado con id: " + id);
        }
    }
}

