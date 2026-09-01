package com.antoinevignes.missionmanagement.dto.operator;

import com.antoinevignes.missionmanagement.entity.operator.OperatorStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record OperatorResponse(
    UUID id,
    String firstName,
    String lastName,
    String licenseNumber,
    LocalDate licenseExpiration,
    String phone,
    String email,
    OperatorStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
