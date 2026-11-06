INSERT INTO participant (full_name) VALUES
                                        ('Иван Иванов'),
                                        ('Мария Петрова'),
                                        ('Сергей Сидоров')
    ON CONFLICT (full_name) DO NOTHING;

INSERT INTO qr_code (code, active, participant_id)
SELECT 'a1b2c3d4-0001-0000-0000-000000000001'::uuid, true, id
FROM participant WHERE full_name = 'Иван Иванов'
    ON CONFLICT (code) DO NOTHING;

INSERT INTO qr_code (code, active, participant_id)
SELECT 'a1b2c3d4-0002-0000-0000-000000000002'::uuid, true, id
FROM participant WHERE full_name = 'Мария Петрова'
    ON CONFLICT (code) DO NOTHING;

INSERT INTO qr_code (code, active, participant_id)
SELECT 'a1b2c3d4-0003-0000-0000-000000000003'::uuid, true, id
FROM participant WHERE full_name = 'Сергей Сидоров'
    ON CONFLICT (code) DO NOTHING;

