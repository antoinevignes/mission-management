package com.antoinevignes.missionmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.antoinevignes.missionmanagement.dto.aircraft.AircraftResponse;
import com.antoinevignes.missionmanagement.dto.aircraft.CreateAircraftRequest;
import com.antoinevignes.missionmanagement.entity.aircraft.Aircraft;
import com.antoinevignes.missionmanagement.entity.aircraft.AircraftStatus;
import com.antoinevignes.missionmanagement.entity.aircraft.AircraftType;
import com.antoinevignes.missionmanagement.exception.aircraft.AircraftAlreadyExistsException;
import com.antoinevignes.missionmanagement.exception.aircraft.AircraftNotFoundException;
import com.antoinevignes.missionmanagement.mapper.AircraftMapper;
import com.antoinevignes.missionmanagement.repository.AircraftRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {

  @Mock private AircraftRepository aircraftRepository;

  @Mock private AircraftMapper aircraftMapper;

  @InjectMocks private AircraftService aircraftService;

  private Aircraft createAircraft(UUID id) {
    return Aircraft.builder()
        .id(id)
        .registration("F-HABC")
        .name("Gribouille")
        .type(AircraftType.FIXED_WING)
        .manufacturer("Airbus")
        .model("A320")
        .serialNumber("ABC123")
        .status(AircraftStatus.AVAILABLE)
        .maxFlightTimeMinutes(10)
        .maxPayloadKg(BigDecimal.valueOf(9000))
        .build();
  }

  private CreateAircraftRequest createAircraftRequest() {
    return new CreateAircraftRequest(
        "f-habc",
        "Gribouille",
        AircraftType.FIXED_WING,
        "Airbus",
        "A320",
        "abc123",
        10,
        BigDecimal.valueOf(9000));
  }

  private CreateAircraftRequest updateAircraftRequest() {
    return new CreateAircraftRequest(
        "f-hxyz",
        "Nouvelle Gribouille",
        AircraftType.FIXED_WING,
        "Airbus",
        "A321",
        "xyz789",
        12,
        BigDecimal.valueOf(9500));
  }

  private AircraftResponse createAircraftResponse(Aircraft aircraft) {
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

  //  CREATE AIRCRAFT TEST
  @Test
  void shouldCreateAircraft() {

    UUID id = UUID.randomUUID();
    CreateAircraftRequest request = createAircraftRequest();
    Aircraft aircraft = createAircraft(id);
    AircraftResponse expectedResponse = createAircraftResponse(aircraft);

    when(aircraftRepository.existsByRegistration("F-HABC")).thenReturn(false);
    when(aircraftRepository.existsBySerialNumber("ABC123")).thenReturn(false);
    when(aircraftMapper.toEntity(request)).thenReturn(aircraft);
    when(aircraftRepository.save(aircraft)).thenReturn(aircraft);
    when(aircraftMapper.toResponse(aircraft)).thenReturn(expectedResponse);

    AircraftResponse response = aircraftService.createAircraft(request);

    assertThat(response).isEqualTo(expectedResponse);

    verify(aircraftRepository).existsByRegistration("F-HABC");
    verify(aircraftRepository).existsBySerialNumber("ABC123");
    verify(aircraftRepository).save(aircraft);
    verify(aircraftMapper).toEntity(request);
    verify(aircraftMapper).toResponse(aircraft);
  }

  //  REGISTRATION EXCEPTION TEST
  @Test
  void shouldThrowExceptionWhenRegistrationAlreadyExists() {

    CreateAircraftRequest request = createAircraftRequest();

    when(aircraftRepository.existsByRegistration("F-HABC")).thenReturn(true);

    assertThatThrownBy(() -> aircraftService.createAircraft(request))
        .isInstanceOf(AircraftAlreadyExistsException.class);

    verify(aircraftRepository).existsByRegistration("F-HABC");
    verify(aircraftRepository, never()).save(any());
  }

  //  SERIAL NUMBER EXCEPTION TEST
  @Test
  void shouldThrowExceptionWhenSerialNumberAlreadyExists() {

    CreateAircraftRequest request = createAircraftRequest();

    when(aircraftRepository.existsBySerialNumber("ABC123")).thenReturn(true);

    assertThatThrownBy(() -> aircraftService.createAircraft(request))
        .isInstanceOf(AircraftAlreadyExistsException.class);

    verify(aircraftRepository).existsBySerialNumber("ABC123");
    verify(aircraftRepository, never()).save(any());
  }

  //  LIST ALL TEST
  @Test
  void shouldListAircraft() {

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();
    Aircraft firstAircraft = createAircraft(firstId);
    Aircraft secondAircraft = createAircraft(secondId);
    AircraftResponse firstResponse = createAircraftResponse(firstAircraft);
    AircraftResponse secondResponse = createAircraftResponse(secondAircraft);

    when(aircraftRepository.findAll()).thenReturn(List.of(firstAircraft, secondAircraft));
    when(aircraftMapper.toResponse(firstAircraft)).thenReturn(firstResponse);
    when(aircraftMapper.toResponse(secondAircraft)).thenReturn(secondResponse);

    List<AircraftResponse> responses = aircraftService.listAircraft();

    assertThat(responses).containsExactly(firstResponse, secondResponse);

    verify(aircraftRepository).findAll();
    verify(aircraftMapper).toResponse(firstAircraft);
    verify(aircraftMapper).toResponse(secondAircraft);
  }

  //  GET BY ID TEST
  @Test
  void shouldGetAircraftById() {

    UUID id = UUID.randomUUID();
    Aircraft aircraft = createAircraft(id);
    AircraftResponse expectedResponse = createAircraftResponse(aircraft);

    when(aircraftRepository.findById(id)).thenReturn(Optional.of(aircraft));
    when(aircraftMapper.toResponse(aircraft)).thenReturn(expectedResponse);

    AircraftResponse response = aircraftService.getAircraftById(id);

    assertThat(response).isEqualTo(expectedResponse);

    verify(aircraftRepository).findById(id);
    verify(aircraftMapper).toResponse(aircraft);
  }

  //  AIRCRAFT NOT FOUND EXCEPTION TEST
  @Test
  void shouldThrowExceptionWhenAircraftNotFound() {

    UUID id = UUID.randomUUID();

    when(aircraftRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> aircraftService.getAircraftById(id))
        .isInstanceOf(AircraftNotFoundException.class);

    verify(aircraftRepository).findById(id);
    verify(aircraftMapper, never()).toResponse(any());
  }

  //  UPDATE AIRCRAFT TEST
  @Test
  void shouldUpdateAircraftById() {

    UUID id = UUID.randomUUID();
    Aircraft aircraft = createAircraft(id);
    CreateAircraftRequest request = updateAircraftRequest();
    AircraftResponse expectedResponse = createAircraftResponse(aircraft);

    when(aircraftRepository.findById(id)).thenReturn(Optional.of(aircraft));
    when(aircraftRepository.existsByRegistrationAndIdNot("F-HXYZ", id)).thenReturn(false);
    when(aircraftRepository.existsBySerialNumberAndIdNot("XYZ789", id)).thenReturn(false);
    when(aircraftMapper.update(aircraft, request)).thenReturn(aircraft);
    when(aircraftRepository.save(aircraft)).thenReturn(aircraft);
    when(aircraftMapper.toResponse(aircraft)).thenReturn(expectedResponse);

    AircraftResponse response = aircraftService.updateAircraftById(id, request);

    assertThat(response).isEqualTo(expectedResponse);

    verify(aircraftRepository).findById(id);
    verify(aircraftRepository).existsByRegistrationAndIdNot("F-HXYZ", id);
    verify(aircraftRepository).existsBySerialNumberAndIdNot("XYZ789", id);
    verify(aircraftMapper).update(aircraft, request);
    verify(aircraftRepository).save(aircraft);
    verify(aircraftMapper).toResponse(aircraft);
  }

  //  EXISTS BY REGISTRATION AND ID NOT TEST
  @Test
  void shouldThrowExceptionWhenExistsByRegistrationAndIdNot() {

    UUID id = UUID.randomUUID();
    Aircraft aircraft = createAircraft(id);
    CreateAircraftRequest request = updateAircraftRequest();

    when(aircraftRepository.findById(id)).thenReturn(Optional.of(aircraft));
    when(aircraftRepository.existsByRegistrationAndIdNot("F-HXYZ", id)).thenReturn(true);

    assertThatThrownBy(() -> aircraftService.updateAircraftById(id, request))
        .isInstanceOf(AircraftAlreadyExistsException.class);

    verify(aircraftRepository).findById(id);
    verify(aircraftRepository).existsByRegistrationAndIdNot("F-HXYZ", id);
    verify(aircraftRepository, never()).save(any());
    verify(aircraftRepository, never()).existsBySerialNumberAndIdNot(anyString(), any());
  }

  //  EXISTS BY SERIAL NUMBER AND ID NOT TEST
  @Test
  void shouldThrowExceptionWhenExistsBySerialNumberAndIdNot() {

    UUID id = UUID.randomUUID();
    Aircraft aircraft = createAircraft(id);
    CreateAircraftRequest request = updateAircraftRequest();

    when(aircraftRepository.findById(id)).thenReturn(Optional.of(aircraft));
    when(aircraftRepository.existsByRegistrationAndIdNot("F-HXYZ", id)).thenReturn(false);
    when(aircraftRepository.existsBySerialNumberAndIdNot("XYZ789", id)).thenReturn(true);

    assertThatThrownBy(() -> aircraftService.updateAircraftById(id, request))
        .isInstanceOf(AircraftAlreadyExistsException.class);

    verify(aircraftRepository).findById(id);
    verify(aircraftRepository).existsByRegistrationAndIdNot("F-HXYZ", id);
    verify(aircraftRepository).existsBySerialNumberAndIdNot("XYZ789", id);
    verify(aircraftRepository, never()).save(any());
  }

  //  DELETE BY ID TEST
  @Test
  void shouldDeleteAircraftById() {

    UUID id = UUID.randomUUID();
    Aircraft aircraft = createAircraft(id);

    when(aircraftRepository.findById(id)).thenReturn(Optional.of(aircraft));

    aircraftService.deleteAircraftById(id);

    verify(aircraftRepository).findById(id);
    verify(aircraftRepository).delete(aircraft);
  }
}
