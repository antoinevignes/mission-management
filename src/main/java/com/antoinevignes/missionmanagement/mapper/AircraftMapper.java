package com.antoinevignes.missionmanagement.mapper;

import com.antoinevignes.missionmanagement.dto.aircraft.AircraftResponse;
import com.antoinevignes.missionmanagement.dto.aircraft.CreateAircraftRequest;
import com.antoinevignes.missionmanagement.entity.aircraft.Aircraft;
import com.antoinevignes.missionmanagement.entity.aircraft.AircraftStatus;
import org.springframework.stereotype.Component;

@Component
public class AircraftMapper {

  public Aircraft toEntity(CreateAircraftRequest request) {
    return Aircraft.builder()
        .registration(request.registration())
        .name(request.name())
        .type(request.type())
        .manufacturer(request.manufacturer())
        .model(request.model())
        .serialNumber(request.serialNumber())
        .status(AircraftStatus.AVAILABLE)
        .maxFlightTime(request.maxFlightTime())
        .maxPayload(request.maxPayload())
        .build();
  }

  public AircraftResponse toResponse(Aircraft aircraft) {
    return new AircraftResponse(
        aircraft.getId(),
        aircraft.getRegistration(),
        aircraft.getName(),
        aircraft.getType(),
        aircraft.getManufacturer(),
        aircraft.getModel(),
        aircraft.getSerialNumber(),
        aircraft.getStatus(),
        aircraft.getMaxFlightTime(),
        aircraft.getMaxPayload(),
        aircraft.getCreatedAt(),
        aircraft.getUpdatedAt());
  }
}
