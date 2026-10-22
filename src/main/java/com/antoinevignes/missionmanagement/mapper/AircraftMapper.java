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
        .maxFlightTimeMinutes(request.maxFlightTimeMinutes())
        .maxPayloadKg(request.maxPayloadKg())
        .build();
  }

  public Aircraft update(Aircraft aircraft, CreateAircraftRequest request) {

    aircraft.setRegistration(request.registration());
    aircraft.setName(request.name());
    aircraft.setType(request.type());
    aircraft.setManufacturer(request.manufacturer());
    aircraft.setModel(request.model());
    aircraft.setSerialNumber(request.serialNumber());
    aircraft.setMaxFlightTimeMinutes(request.maxFlightTimeMinutes());
    aircraft.setMaxPayloadKg(request.maxPayloadKg());

    return aircraft;
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
        aircraft.getMaxFlightTimeMinutes(),
        aircraft.getMaxPayloadKg(),
        aircraft.getCreatedAt(),
        aircraft.getUpdatedAt());
  }
}
