CREATE UNIQUE INDEX IF NOT EXISTS uq_participant_full_name
    ON participant (full_name);

CREATE UNIQUE INDEX IF NOT EXISTS uq_one_active_qr_per_participant
    ON qr_code (participant_id) WHERE active = true;