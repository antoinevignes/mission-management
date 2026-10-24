package com.antoinevignes.missionmanagement.mapper;

import com.antoinevignes.missionmanagement.dto.mission.CreateMissionRequest;
import com.antoinevignes.missionmanagement.dto.mission.MissionResponse;
import com.antoinevignes.missionmanagement.entity.aircraft.Aircraft;
import com.antoinevignes.missionmanagement.entity.mission.Mission;
import com.antoinevignes.missionmanagement.entity.mission.MissionStatus;
import com.antoinevignes.missionmanagement.entity.operator.Operator;
import org.springframework.stereotype.Component;

@Component
public class MissionMapper {

  public Mission toEntity(CreateMissionRequest request, Aircraft aircraft, Operator operator) {

    return Mission.builder()
        .code(request.code())
        .title(request.title())
        .aircraft(aircraft)
        .operator(operator)
        .departureAirport(request.departureAirport())
        .arrivalAirport(request.arrivalAirport())
        .plannedStart(request.plannedStart())
        .plannedEnd(request.plannedEnd())
        .payloadKg(request.payloadKg())
        .status(MissionStatus.PLANNED)
        .build();
  }

  public Mission update(
      Mission mission, CreateMissionRequest request, Aircraft aircraft, Operator operator) {

    mission.setCode(request.code());
    mission.setTitle(request.title());
    mission.setAircraft(aircraft);
    mission.setOperator(operator);
    mission.setDepartureAirport(request.departureAirport());
    mission.setArrivalAirport(request.arrivalAirport());
    mission.setPlannedStart(request.plannedStart());
    mission.setPlannedEnd(request.plannedEnd());
    mission.setPayloadKg(request.payloadKg());

    return mission;
  }

  public MissionResponse toResponse(Mission mission) {

    return new MissionResponse(
        mission.getId(),
        mission.getCode(),
        mission.getTitle(),
        mission.getAircraft().getId(),
        mission.getAircraft().getRegistration(),
        mission.getOperator().getId(),
        mission.getOperator().getFirstName() + " " + mission.getOperator().getLastName(),
        mission.getDepartureAirport(),
        mission.getArrivalAirport(),
        mission.getPlannedStart(),
        mission.getPlannedEnd(),
        mission.getPayloadKg(),
        mission.getStatus(),
        mission.getCreatedAt(),
        mission.getUpdatedAt());
  }
}
