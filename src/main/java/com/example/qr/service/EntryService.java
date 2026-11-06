package com.example.qr.service;

import com.example.qr.dto.EntryResponse;
import com.example.qr.entity.Participant;
import com.example.qr.entity.QrCode;
import com.example.qr.exception.EntryDeniedException;
import com.example.qr.repository.QrCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EntryService {

    private final QrCodeRepository qrCodeRepository;

    public EntryService(QrCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    @Transactional
    public EntryResponse enter(UUID code) {
        QrCode qr = qrCodeRepository.findByCodeAndActiveTrueForUpdate(code)
                .orElseThrow(() -> new EntryDeniedException("QR code is invalid or already used"));

        Participant participant = qr.getParticipant();

        qr.setActive(false);
        qrCodeRepository.flush();          // UPDATE до INSERT
        QrCode fresh = new QrCode(UUID.randomUUID(), true, participant);
        qrCodeRepository.save(fresh);

        return new EntryResponse(participant.getFullName(), fresh.getCode());
    }





}