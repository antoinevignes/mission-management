package com.antoinevignes.missionmanagement.controller;

import com.antoinevignes.missionmanagement.dto.aircraft.AircraftResponse;
import com.antoinevignes.missionmanagement.dto.aircraft.CreateAircraftRequest;
import com.antoinevignes.missionmanagement.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/aircrafts")
@RequiredArgsConstructor
public class AircraftController {

  private final AircraftService aircraftService;

  @PostMapping
  public ResponseEntity<AircraftResponse> createAircraft(
      @Valid @RequestBody CreateAircraftRequest request) {

    AircraftResponse response = aircraftService.createAircraft(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
