package com.example.qr.exception;

public class ActiveQrNotFoundException extends RuntimeException {
    public ActiveQrNotFoundException(Long participantId) {
        super("No active QR for participant id=" + participantId);
    }
}