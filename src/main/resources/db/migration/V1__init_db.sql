CREATE TABLE sinnts_department
(
    id                 UUID NOT NULL,
    name               VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_sinnts_department PRIMARY KEY (id)
);

CREATE TABLE sinnts_grading_admin
(
    id                 UUID         NOT NULL,
    full_name          VARCHAR(255),
    username           VARCHAR(255) NOT NULL,
    password           VARCHAR(255),
    account_locked     BOOLEAN      NOT NULL,
    enabled            BOOLEAN      NOT NULL,
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_sinnts_grading_admin PRIMARY KEY (id)
);

CREATE TABLE sinnts_staff
(
    id                 UUID NOT NULL,
    full_name          VARCHAR(255),
    title              VARCHAR(255),
    identity           VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
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
    staff_id           UUID,
    CONSTRAINT pk_sinnts_staff_grading PRIMARY KEY (id)
);

CREATE TABLE sinnts_staff_performance
(
    id                 UUID NOT NULL,
    name               VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_sinnts_staff_performance PRIMARY KEY (id)
);

ALTER TABLE sinnts_grading_admin
    ADD CONSTRAINT uc_sinnts_grading_admin_username UNIQUE (username);

ALTER TABLE sinnts_staff_grading
    ADD CONSTRAINT uc_sinnts_staff_grading_performance UNIQUE (performance_id);

ALTER TABLE sinnts_staff_grading
    ADD CONSTRAINT FK_SINNTS_STAFF_GRADING_ON_PERFORMANCE FOREIGN KEY (performance_id) REFERENCES sinnts_staff_performance (id);

ALTER TABLE sinnts_staff_grading
    ADD CONSTRAINT FK_SINNTS_STAFF_GRADING_ON_STAFF FOREIGN KEY (staff_id) REFERENCES sinnts_staff (id);

ALTER TABLE sinnts_staff
    ADD CONSTRAINT FK_SINNTS_STAFF_ON_DEPARTMENT FOREIGN KEY (department_id) REFERENCES sinnts_department (id);