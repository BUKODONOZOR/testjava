package com.prueba.main.repository;

import com.prueba.main.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChargeRepository extends JpaRepository<Charge , Long> {
    List<Charge> findByPalletId(Long palletId);
    List<Charge> findByCarrier_Id(Long carrierId);


}
