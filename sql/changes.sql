-- Dharmesh 08/25/2024
ALTER TABLE doctor_appointment MODIFY schedule_type TINYINT NOT NULL;
ALTER TABLE doctor_appointment ADD COLUMN revenue BIGINT NOT NULL;