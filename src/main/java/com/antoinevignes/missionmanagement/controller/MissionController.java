package com.antoinevignes.missionmanagement.controller;

import com.antoinevignes.missionmanagement.dto.mission.CreateMissionRequest;
import com.antoinevignes.missionmanagement.dto.mission.MissionResponse;
import com.antoinevignes.missionmanagement.service.MissionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/missions")
@RequiredArgsConstructor
public class MissionController {

  private final MissionService missionService;

  @PostMapping
  public ResponseEntity<MissionResponse> createMission(
      @Valid @RequestBody CreateMissionRequest request) {

    MissionResponse response = missionService.createMission(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<MissionResponse>> listMissions() {

    return ResponseEntity.ok(missionService.listMissions());
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<MissionResponse> getMissionById(@PathVariable UUID id) {

    return ResponseEntity.ok(missionService.getMissionById(id));
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<MissionResponse> updateMissionById(
      @PathVariable UUID id, @Valid @RequestBody CreateMissionRequest request) {

    MissionResponse response = missionService.updateMissionById(id, request);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> deleteMissionById(@PathVariable UUID id) {

    missionService.deleteMissionById(id);

    return ResponseEntity.noContent().build();
  }
}
