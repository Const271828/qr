package com.example.qr.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EntryRequest(
        @NotNull(message = "code must not be null")
        UUID code
) {
}