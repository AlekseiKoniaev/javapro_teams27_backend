ALTER TABLE person ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE NOT NULL;
ALTER TABLE person ADD COLUMN deleted_time TIMESTAMP;
