package com.example.qr.service;

import com.example.qr.dto.EntryResponse;
import com.example.qr.dto.ParticipantRequest;
import com.example.qr.dto.ParticipantResponse;
import com.example.qr.dto.QrCodeResponse;
import com.example.qr.exception.EntryDeniedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EntryServiceTest {

    @Autowired
    private EntryService entryService;

    @Autowired
    private ParticipantService participantService;

    @Test
    void enter_SuccessAndOneTimeUse() {
        // 1. Создаем участника (чтобы тест не зависел от того, кто лежит в БД под id=1)
        ParticipantResponse newParticipant = participantService.create(new ParticipantRequest("Тестовый Пользователь"));

        // 2. Получаем его активный QR
        QrCodeResponse activeQr = participantService.findActiveQr(newParticipant.id());
        UUID oldCode = activeQr.code();

        // 3. Успешный вход
        EntryResponse response = entryService.enter(oldCode);

        assertEquals("Тестовый Пользователь", response.fullName());
        assertNotNull(response.newCode());

        // 4. Повторный вход старым кодом — должен упасть (одноразовость)
        assertThrows(EntryDeniedException.class, () -> entryService.enter(oldCode));

        // 5. Новый код должен работать
        EntryResponse secondResponse = entryService.enter(response.newCode());
        assertEquals("Тестовый Пользователь", secondResponse.fullName());
    }
}