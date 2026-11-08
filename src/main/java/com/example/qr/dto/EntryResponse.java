package com.example.qr.dto;

import java.util.UUID;

public record EntryResponse(String fullName, UUID newCode) {
}