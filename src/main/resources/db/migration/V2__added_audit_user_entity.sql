ALTER TABLE sinnts_users
    ADD created_by UUID;

ALTER TABLE sinnts_users
    ADD last_modified_by UUID;

ALTER TABLE sinnts_users
    ALTER COLUMN created_by SET NOT NULL;

ALTER TABLE sinnts_users
    ADD CONSTRAINT FK_SINNTS_USERS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES sinnts_users (id);

ALTER TABLE sinnts_users
    ADD CONSTRAINT FK_SINNTS_USERS_ON_LAST_MODIFIED_BY FOREIGN KEY (last_modified_by) REFERENCES sinnts_users (id);