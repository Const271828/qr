-- Добавляем участников с фиксированными ID, если уже есть, то ничего не делаем.
INSERT INTO participant (id, full_name) VALUES
                                            (1, 'Иван Иванов'),
                                            (2, 'Мария Петрова'),
                                            (3, 'Сергей Сидоров')
    ON CONFLICT (id) DO NOTHING;

-- Синхронизируем sequence, чтобы при создании новых не пыталась выдать id=1,2,3
SELECT setval('participant_id_seq', COALESCE((SELECT MAX(id) FROM participant), 1));

-- Выдаем QR-коды если нет активного
INSERT INTO qr_code (code, active, participant_id)
SELECT gen_random_uuid(), true, 1
    WHERE NOT EXISTS (
    SELECT 1 FROM qr_code WHERE participant_id = 1 AND active = true
);

INSERT INTO qr_code (code, active, participant_id)
SELECT gen_random_uuid(), true, 2
    WHERE NOT EXISTS (
    SELECT 1 FROM qr_code WHERE participant_id = 2 AND active = true
);

INSERT INTO qr_code (code, active, participant_id)
SELECT gen_random_uuid(), true, 3
    WHERE NOT EXISTS (
    SELECT 1 FROM qr_code WHERE participant_id = 3 AND active = true
);