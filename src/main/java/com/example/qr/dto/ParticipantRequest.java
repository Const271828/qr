package com.example.qr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ParticipantRequest(
        @NotBlank(message = "fullName must not be blank")
        @Size(max = 255, message = "fullName must be at most 255 characters")
        String fullName
) {
}