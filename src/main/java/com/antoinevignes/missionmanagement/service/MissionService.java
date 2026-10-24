package com.antoinevignes.missionmanagement.service;

import com.antoinevignes.missionmanagement.dto.mission.CreateMissionRequest;
import com.antoinevignes.missionmanagement.dto.mission.MissionResponse;
import com.antoinevignes.missionmanagement.entity.aircraft.Aircraft;
import com.antoinevignes.missionmanagement.entity.mission.Mission;
import com.antoinevignes.missionmanagement.entity.operator.Operator;
import com.antoinevignes.missionmanagement.exception.aircraft.AircraftNotFoundException;
import com.antoinevignes.missionmanagement.exception.mission.MissionAlreadyExistsException;
import com.antoinevignes.missionmanagement.exception.mission.MissionNotFoundException;
import com.antoinevignes.missionmanagement.exception.operator.OperatorNotFoundException;
import com.antoinevignes.missionmanagement.mapper.MissionMapper;
import com.antoinevignes.missionmanagement.repository.AircraftRepository;
import com.antoinevignes.missionmanagement.repository.MissionRepository;
import com.antoinevignes.missionmanagement.repository.OperatorRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {

  private final MissionRepository missionRepository;
  private final AircraftRepository aircraftRepository;
  private final OperatorRepository operatorRepository;
  private final MissionMapper missionMapper;

  //  CREATE MISSION
  @Transactional
  public MissionResponse createMission(CreateMissionRequest request) {

    if (missionRepository.existsByCode(request.code())) {
      throw new MissionAlreadyExistsException(
          "Mission with code '" + request.code() + "' already exists.");
    }

    Aircraft aircraft = findAircraftById(request.aircraftId());
    Operator operator = findOperatorById(request.operatorId());
    Mission mission = missionMapper.toEntity(request, aircraft, operator);

    Mission savedMission = missionRepository.save(mission);

    return missionMapper.toResponse(savedMission);
  }

  //  LIST ALL MISSIONS
  @Transactional(readOnly = true)
  public List<MissionResponse> listMissions() {

    List<Mission> missions = missionRepository.findAll();

    return missions.stream().map(missionMapper::toResponse).toList();
  }

  //  GET MISSION BY ID
  @Transactional(readOnly = true)
  public MissionResponse getMissionById(UUID id) {

    Mission mission =
        missionRepository
            .findById(id)
            .orElseThrow(
                () -> new MissionNotFoundException("Mission with id '" + id + "' not found."));

    return missionMapper.toResponse(mission);
  }

  //  UPDATE MISSION
  @Transactional
  public MissionResponse updateMissionById(UUID id, CreateMissionRequest request) {

    Mission mission =
        missionRepository
            .findById(id)
            .orElseThrow(
                () -> new MissionNotFoundException("Mission with id '" + id + "' not found."));

    if (missionRepository.existsByCodeAndIdNot(request.code(), id)) {
      throw new MissionAlreadyExistsException(
          "Mission with code '" + request.code() + "' already exists.");
    }

    Aircraft aircraft = findAircraftById(request.aircraftId());
    Operator operator = findOperatorById(request.operatorId());
    missionMapper.update(mission, request, aircraft, operator);

    Mission savedMission = missionRepository.saveAndFlush(mission);

    return missionMapper.toResponse(savedMission);
  }

  //  DELETE MISSION
  @Transactional
  public void deleteMissionById(UUID id) {

    Mission mission =
        missionRepository
            .findById(id)
            .orElseThrow(
                () -> new MissionNotFoundException("Mission with id '" + id + "' not found."));

    missionRepository.delete(mission);
  }

  //  HELPERS
  private Aircraft findAircraftById(UUID id) {

    return aircraftRepository
        .findById(id)
        .orElseThrow(
            () -> new AircraftNotFoundException("Aircraft with id '" + id + "' not found."));
  }

  private Operator findOperatorById(UUID id) {

    return operatorRepository
        .findById(id)
        .orElseThrow(
            () -> new OperatorNotFoundException("Operator with id '" + id + "' not found."));
  }
}
