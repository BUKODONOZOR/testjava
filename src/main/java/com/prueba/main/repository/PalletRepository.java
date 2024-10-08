package com.prueba.main.repository;

import com.prueba.main.entity.Pallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalletRepository extends JpaRepository <Pallet , Long> {

}
