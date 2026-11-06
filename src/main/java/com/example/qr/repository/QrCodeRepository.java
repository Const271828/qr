package com.example.qr.repository;

import com.example.qr.entity.QrCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select q from QrCode q where q.code = :code and q.active = true")
    Optional<QrCode> findByCodeAndActiveTrueForUpdate(@Param("code") UUID code);

    @Query("select q from QrCode q where q.participant.id = :participantId and q.active = true")
    Optional<QrCode> findActiveByParticipantId(@Param("participantId") Long participantId);
}