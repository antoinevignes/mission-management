package com.antoinevignes.missionmanagement.service;

import com.antoinevignes.missionmanagement.dto.aircraft.AircraftResponse;
import com.antoinevignes.missionmanagement.dto.aircraft.CreateAircraftRequest;
import com.antoinevignes.missionmanagement.entity.aircraft.Aircraft;
import com.antoinevignes.missionmanagement.exception.aircraft.AircraftAlreadyExistsException;
import com.antoinevignes.missionmanagement.mapper.AircraftMapper;
import com.antoinevignes.missionmanagement.repository.AircraftRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AircraftService {

  private final AircraftRepository aircraftRepository;
  private final AircraftMapper aircraftMapper;

  public AircraftResponse createAircraft(CreateAircraftRequest request) {

    if (aircraftRepository.existsByRegistration(request.registration())) {
      throw new AircraftAlreadyExistsException(
          "Aircraft with registration '" + request.registration() + "' already exists.");
    }

    if (aircraftRepository.existsBySerialNumber(request.serialNumber())) {
      throw new AircraftAlreadyExistsException(
          "Aircraft with serial number '" + request.serialNumber() + "' already exists.");
    }

    Aircraft aircraft = aircraftMapper.toEntity(request);

    Aircraft savedAircraft = aircraftRepository.save(aircraft);

    return aircraftMapper.toResponse(savedAircraft);
  }

  public List<AircraftResponse> listAircraft() {
    List<Aircraft> aircraft = aircraftRepository.findAll();

    return aircraft.stream().map(aircraftMapper::toResponse).toList();
  }
}
