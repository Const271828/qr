package com.example.qr.exception;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(Long id) {
        super("Participant not found: id=" + id);
    }
}