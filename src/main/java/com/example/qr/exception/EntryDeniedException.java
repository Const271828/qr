package com.example.qr.exception;

public class EntryDeniedException extends RuntimeException {
    public EntryDeniedException(String message) {
        super(message);
    }
}