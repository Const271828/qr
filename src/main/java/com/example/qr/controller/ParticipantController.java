package com.example.qr.controller;

import com.example.qr.dto.ParticipantRequest;
import com.example.qr.dto.ParticipantResponse;
import com.example.qr.dto.QrCodeHistoryResponse;
import com.example.qr.dto.QrCodeResponse;
import com.example.qr.service.ParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping
    public List<ParticipantResponse> findAll() {
        return participantService.findAll();
    }

    @GetMapping("/{id}")
    public ParticipantResponse findById(@PathVariable Long id) {
        return participantService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipantResponse create(@Valid @RequestBody ParticipantRequest request) {
        return participantService.create(request);
    }

    @PutMapping("/{id}")
    public ParticipantResponse update(@PathVariable Long id,
                                      @Valid @RequestBody ParticipantRequest request) {
        return participantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        participantService.delete(id);
    }

    @PostMapping("/{id}/qr")
    public ResponseEntity<UUID> reissueQr(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.reissueQr(id));
    }

    @GetMapping("/{id}/qr")
    public QrCodeResponse findActiveQr(@PathVariable Long id) {
        return participantService.findActiveQr(id);
    }

    @DeleteMapping("/{id}/qr")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeQr(@PathVariable Long id) {
        participantService.revokeQr(id);
    }

    @GetMapping("/{id}/qr/history")
    public List<QrCodeHistoryResponse> findAllQrCodes(@PathVariable Long id) {
        return participantService.findAllQrCodes(id);
    }
}