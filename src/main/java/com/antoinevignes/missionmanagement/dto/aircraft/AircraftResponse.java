package com.antoinevignes.missionmanagement.dto.aircraft;

import com.antoinevignes.missionmanagement.entity.aircraft.AircraftStatus;
import com.antoinevignes.missionmanagement.entity.aircraft.AircraftType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AircraftResponse(
    UUID id,
    String registration,
    String name,
    AircraftType type,
    String manufacturer,
    String model,
    String serialNumber,
    AircraftStatus status,
    Integer maxFlightTimeMinutes,
    BigDecimal maxPayloadKg,
    Instant createdAt,
    Instant updatedAt) {}
