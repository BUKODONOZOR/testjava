package com.prueba.main.repository;

import com.prueba.main.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrierRepository extends JpaRepository <Carrier , Long> {
    Optional<Carrier> findByUsername(String username);
    Optional<Carrier> findByName(String email);
    boolean existsByUsername(String Username);

}
