package com.example.qr.dto;

import java.util.UUID;

public record QrCodeHistoryResponse(UUID code, boolean active) {
}