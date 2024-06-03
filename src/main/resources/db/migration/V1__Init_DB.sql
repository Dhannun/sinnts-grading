CREATE TABLE sinnts_access_tokens
(
    id         UUID    NOT NULL,
    token      VARCHAR(255),
    token_type VARCHAR(255),
    expired    BOOLEAN NOT NULL,
    revoked    BOOLEAN NOT NULL,
    user_id    UUID,
    CONSTRAINT pk_sinnts_access_tokens PRIMARY KEY (id)
);

CREATE TABLE sinnts_department
(
    id                 UUID NOT NULL,
    name               VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    created_by         UUID NOT NULL,
    last_modified_by   UUID,
    CONSTRAINT pk_sinnts_department PRIMARY KEY (id)
);

CREATE TABLE sinnts_department_performances
(
    department_id   UUID NOT NULL,
    performances_id UUID NOT NULL,
    CONSTRAINT pk_sinnts_department_performances PRIMARY KEY (department_id, performances_id)
);

CREATE TABLE sinnts_staff
(
    id                 UUID NOT NULL,
    full_name          VARCHAR(255),
    title              VARCHAR(255),
    identity           VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    created_by         UUID NOT NULL,
    last_modified_by   UUID,
    department_id      UUID NOT NULL,
    CONSTRAINT pk_sinnts_staff PRIMARY KEY (id)
);

CREATE TABLE sinnts_staff_grading
(
    id                 UUID NOT NULL,
    performance_id     UUID,
    grade              VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    created_by         UUID NOT NULL,
    last_modified_by   UUID,
    staff_id           UUID,
    CONSTRAINT pk_sinnts_staff_grading PRIMARY KEY (id)
);

CREATE TABLE sinnts_staff_performance
(
    id                 UUID NOT NULL,
    name               VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    created_by         UUID NOT NULL,
    last_modified_by   UUID,
    CONSTRAINT pk_sinnts_staff_performance PRIMARY KEY (id)
);

CREATE TABLE sinnts_users
(
    id                 UUID         NOT NULL,
    full_name          VARCHAR(255),
    username           VARCHAR(255) NOT NULL,
    password           VARCHAR(255),
    role               VARCHAR(255),
    enabled            BOOLEAN      NOT NULL,
    locked             BOOLEAN      NOT NULL,
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_sinnts_users PRIMARY KEY (id)
);

ALTER TABLE sinnts_staff_grading
    ADD CONSTRAINT uc_sinnts_staff_grading_performance UNIQUE (performance_id);

ALTER TABLE sinnts_users
    ADD CONSTRAINT uc_sinnts_users_username UNIQUE (username);

ALTER TABLE sinnts_access_tokens
    ADD CONSTRAINT FK_SINNTS_ACCESS_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES sinnts_users (id);

ALTER TABLE sinnts_staff_grading
    ADD CONSTRAINT FK_SINNTS_STAFF_GRADING_ON_PERFORMANCE FOREIGN KEY (performance_id) REFERENCES sinnts_staff_performance (id);

ALTER TABLE sinnts_staff_grading
    ADD CONSTRAINT FK_SINNTS_STAFF_GRADING_ON_STAFF FOREIGN KEY (staff_id) REFERENCES sinnts_staff (id);

ALTER TABLE sinnts_staff
    ADD CONSTRAINT FK_SINNTS_STAFF_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES sinnts_department (id);

ALTER TABLE sinnts_department_performances
    ADD CONSTRAINT fk_sindepper_on_department FOREIGN KEY (department_id) REFERENCES sinnts_department (id);

ALTER TABLE sinnts_department_performances
    ADD CONSTRAINT fk_sindepper_on_performance FOREIGN KEY (performances_id) REFERENCES sinnts_staff_performance (id);