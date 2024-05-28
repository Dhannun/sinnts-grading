CREATE TABLE sinnts_department_performances
(
    department_id   UUID NOT NULL,
    performances_id UUID NOT NULL,
    CONSTRAINT pk_sinnts_department_performances PRIMARY KEY (department_id, performances_id)
);

ALTER TABLE sinnts_department_performances
    ADD CONSTRAINT uc_sinnts_department_performances_performances UNIQUE (performances_id);

ALTER TABLE sinnts_department_performances
    ADD CONSTRAINT fk_sindepper_on_department FOREIGN KEY (department_id) REFERENCES sinnts_department (id);

ALTER TABLE sinnts_department_performances
    ADD CONSTRAINT fk_sindepper_on_performance FOREIGN KEY (performances_id) REFERENCES sinnts_staff_performance (id);