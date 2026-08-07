package com.antoinevignes.missionmanagement.service;

import com.antoinevignes.missionmanagement.dto.aircraft.AircraftResponse;
import com.antoinevignes.missionmanagement.dto.aircraft.CreateAircraftRequest;
import com.antoinevignes.missionmanagement.dto.aircraft.UpdateAircraftRequest;
import com.antoinevignes.missionmanagement.entity.aircraft.Aircraft;
import com.antoinevignes.missionmanagement.exception.aircraft.AircraftAlreadyExistsException;
import com.antoinevignes.missionmanagement.exception.aircraft.AircraftNotFoundException;
import com.antoinevignes.missionmanagement.mapper.AircraftMapper;
import com.antoinevignes.missionmanagement.repository.AircraftRepository;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AircraftService {

  private final AircraftRepository aircraftRepository;
  private final AircraftMapper aircraftMapper;

  //  CREATE AIRCRAFT
  public AircraftResponse createAircraft(CreateAircraftRequest request) {

    String registration = request.registration().toUpperCase(Locale.ROOT);
    String serialNumber = request.serialNumber().toUpperCase(Locale.ROOT);

    if (aircraftRepository.existsByRegistration(registration)) {
      throw new AircraftAlreadyExistsException(
          "Aircraft with registration '" + registration + "' already exists.");
    }

    if (aircraftRepository.existsBySerialNumber(serialNumber)) {
      throw new AircraftAlreadyExistsException(
          "Aircraft with serial number '" + serialNumber + "' already exists.");
    }

    Aircraft aircraft = aircraftMapper.toEntity(request);

    Aircraft savedAircraft = aircraftRepository.save(aircraft);

    return aircraftMapper.toResponse(savedAircraft);
  }

  //  LIST ALL AIRCRAFT
  public List<AircraftResponse> listAircraft() {

    List<Aircraft> aircraft = aircraftRepository.findAll();

    return aircraft.stream().map(aircraftMapper::toResponse).toList();
  }

  //  GET AIRCRAFT BY ID
  public AircraftResponse getAircraftById(UUID id) {

    Aircraft aircraft =
        aircraftRepository
            .findById(id)
            .orElseThrow(
                () -> new AircraftNotFoundException("Aircraft with id '" + id + "' not found."));

    return aircraftMapper.toResponse(aircraft);
  }

  //  UPDATE AIRCRAFT
  public AircraftResponse updateAircraftById(UUID id, UpdateAircraftRequest request) {

    String registration = request.registration().toUpperCase(Locale.ROOT);
    String serialNumber = request.serialNumber().toUpperCase(Locale.ROOT);

    Aircraft aircraft =
        aircraftRepository
            .findById(id)
            .orElseThrow(
                () -> new AircraftNotFoundException("Aircraft with id '" + id + "' not found."));

    if (aircraftRepository.existsByRegistrationAndIdNot(registration, id)) {
      throw new AircraftAlreadyExistsException(
          "Aircraft with registration '" + registration + "' already exists.");
    }

    if (aircraftRepository.existsBySerialNumberAndIdNot(serialNumber, id)) {
      throw new AircraftAlreadyExistsException(
          "Aircraft with serial number '" + serialNumber + "' already exists.");
    }

    aircraft.setRegistration(registration);
    aircraft.setName(request.name());
    aircraft.setType(request.type());
    aircraft.setManufacturer(request.manufacturer());
    aircraft.setModel(request.model());
    aircraft.setSerialNumber(serialNumber);
    aircraft.setMaxFlightTime(request.maxFlightTime());
    aircraft.setMaxPayload(request.maxPayload());

    Aircraft savedAircraft = aircraftRepository.save(aircraft);

    return aircraftMapper.toResponse(savedAircraft);
  }
}
