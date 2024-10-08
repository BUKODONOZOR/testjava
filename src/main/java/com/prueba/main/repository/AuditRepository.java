package com.prueba.main.repository;

import com.prueba.main.entity.Audit;
import com.prueba.main.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    List<Audit> findByUpdateAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}