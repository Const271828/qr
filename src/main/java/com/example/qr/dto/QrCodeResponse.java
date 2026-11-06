package com.example.qr.dto;

import java.util.UUID;

public record QrCodeResponse(UUID code, boolean active) {
}