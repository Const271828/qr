package com.example.qr.service;

import com.example.qr.dto.ParticipantRequest;
import com.example.qr.dto.ParticipantResponse;
import com.example.qr.dto.QrCodeResponse;
import com.example.qr.entity.Participant;
import com.example.qr.entity.QrCode;
import com.example.qr.exception.ActiveQrNotFoundException;
import com.example.qr.exception.ParticipantNotFoundException;
import com.example.qr.repository.ParticipantRepository;
import com.example.qr.repository.QrCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final QrCodeRepository qrCodeRepository;

    public ParticipantService(ParticipantRepository participantRepository,
                              QrCodeRepository qrCodeRepository) {
        this.participantRepository = participantRepository;
        this.qrCodeRepository = qrCodeRepository;
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> findAll() {
        return participantRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ParticipantResponse findById(Long id) {
        Participant p = participantRepository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException(id));
        return toResponse(p);
    }

    @Transactional
    public ParticipantResponse create(ParticipantRequest request) {
        Participant p = new Participant(request.fullName());
        Participant saved = participantRepository.save(p);
        // сразу выдаём первый активный QR
        QrCode qr = new QrCode(UUID.randomUUID(), true, saved);
        qrCodeRepository.save(qr);
        return toResponse(saved);
    }

    @Transactional
    public ParticipantResponse update(Long id, ParticipantRequest request) {
        Participant p = participantRepository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException(id));
        p.setFullName(request.fullName());
        return toResponse(p); // dirty checking сохранит изменение
    }

    @Transactional
    public void delete(Long id) {
        if (!participantRepository.existsById(id)) {
            throw new ParticipantNotFoundException(id);
        }
        participantRepository.deleteById(id); // cascade удалит QR-коды
    }

    @Transactional
    public UUID reissueQr(Long participantId) {
        Participant p = participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException(participantId));
        // погасить все активные
        p.getQrCodes().stream()
                .filter(QrCode::isActive)
                .forEach(qr -> qr.setActive(false));
        // заставляем Hibernate выполнить UPDATE до INSERT
        qrCodeRepository.flush();
        // выпустить новый
        QrCode fresh = new QrCode(UUID.randomUUID(), true, p);
        p.getQrCodes().add(fresh);
// save не нужен, Hibernate сохранит сам при коммите транзакции
        return fresh.getCode();
    }

    private ParticipantResponse toResponse(Participant p) {
        return new ParticipantResponse(p.getId(), p.getFullName());
    }

    @Transactional(readOnly = true)
    public QrCodeResponse findActiveQr(Long participantId) {
        // Проверяем, что участник вообще существует
        participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException(participantId));

        // Берем только активный код (без загрузки всей истории)
        return qrCodeRepository.findActiveByParticipantId(participantId)
                .map(qr -> new QrCodeResponse(qr.getCode(), true))
                .orElseThrow(() -> new ActiveQrNotFoundException(participantId));
    }

    @Transactional
    public void revokeQr(Long participantId) {
        Participant p = participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException(participantId));

        p.getQrCodes().stream()
                .filter(QrCode::isActive)
                .forEach(qr -> qr.setActive(false));
    }
}