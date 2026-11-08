package com.example.qr.controller;

import com.example.qr.dto.EntryRequest;
import com.example.qr.dto.EntryResponse;
import com.example.qr.service.EntryService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/entry")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping
    public EntryResponse enter(@Valid @RequestBody EntryRequest request) {
        return entryService.enter(request.code());
    }
}