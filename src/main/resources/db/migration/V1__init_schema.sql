CREATE TABLE aircraft
(
    id                      uuid PRIMARY KEY,
    registration            varchar(255)  NOT NULL UNIQUE,
    name                    varchar(255)  NOT NULL,
    type                    varchar(255)  NOT NULL CHECK (type IN ('FIXED_WING', 'ROTARY_WING', 'UAV')),
    manufacturer            varchar(255)  NOT NULL,
    model                   varchar(255)  NOT NULL,
    serial_number           varchar(255)  NOT NULL UNIQUE,
    status                  varchar(255)  NOT NULL CHECK (status IN ('AVAILABLE', 'IN_MISSION', 'MAINTENANCE', 'OUT_OF_SERVICE')),
    max_flight_time_minutes integer       NOT NULL CHECK (max_flight_time_minutes > 0),
    max_payload_kg          numeric(8, 2) NOT NULL CHECK (max_payload_kg > 0),
    created_at              timestamptz   NOT NULL,
    updated_at              timestamptz   NOT NULL
);

CREATE TABLE operator
(
    id                 uuid PRIMARY KEY,
    first_name         varchar(255) NOT NULL,
    last_name          varchar(255) NOT NULL,
    license_number     varchar(255) NOT NULL UNIQUE,
    license_expiration date         NOT NULL,
    phone              varchar(255),
    email              varchar(255) NOT NULL,
    status             varchar(255) NOT NULL CHECK (status IN ('AVAILABLE', 'ON_MISSION', 'OFF_DUTY')),
    created_at         timestamptz  NOT NULL,
    updated_at         timestamptz  NOT NULL
);