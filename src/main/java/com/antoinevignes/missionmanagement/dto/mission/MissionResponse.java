package com.antoinevignes.missionmanagement.dto.mission;

import com.antoinevignes.missionmanagement.entity.mission.MissionStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MissionResponse(
    UUID id,
    String code,
    String title,
    UUID aircraftId,
    String aircraftRegistration,
    UUID operatorId,
    String operatorFullName,
    String departureAirport,
    String arrivalAirport,
    Instant plannedStart,
    Instant plannedEnd,
    BigDecimal payloadKg,
    MissionStatus status,
    Instant createdAt,
    Instant updatedAt) {}
