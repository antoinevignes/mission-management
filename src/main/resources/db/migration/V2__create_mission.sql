CREATE TABLE mission
(
    id                uuid PRIMARY KEY,
    code              varchar(255)  NOT NULL UNIQUE,
    title             varchar(255)  NOT NULL,
    aircraft_id       uuid          NOT NULL REFERENCES aircraft (id),
    operator_id       uuid          NOT NULL REFERENCES operator (id),
    departure_airport varchar(4)    NOT NULL,
    arrival_airport   varchar(4)    NOT NULL,
    planned_start     timestamptz   NOT NULL,
    planned_end       timestamptz   NOT NULL,
    payload_kg        numeric(8, 2) NOT NULL CHECK (payload_kg >= 0),
    status            varchar(255)  NOT NULL CHECK (status IN ('PLANNED', 'BRIEFED', 'IN_FLIGHT', 'COMPLETED', 'ABORTED')),
    created_at        timestamptz   NOT NULL,
    updated_at        timestamptz   NOT NULL,
    CONSTRAINT chk_mission_planned_slot CHECK (planned_end > planned_start)
);

CREATE INDEX idx_mission_aircraft_id ON mission (aircraft_id);
CREATE INDEX idx_mission_operator_id ON mission (operator_id);
