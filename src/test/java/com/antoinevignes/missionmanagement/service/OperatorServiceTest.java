package com.antoinevignes.missionmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.antoinevignes.missionmanagement.dto.operator.CreateOperatorRequest;
import com.antoinevignes.missionmanagement.dto.operator.OperatorResponse;
import com.antoinevignes.missionmanagement.entity.operator.Operator;
import com.antoinevignes.missionmanagement.entity.operator.OperatorStatus;
import com.antoinevignes.missionmanagement.exception.operator.OperatorAlreadyExistsException;
import com.antoinevignes.missionmanagement.exception.operator.OperatorNotFoundException;
import com.antoinevignes.missionmanagement.mapper.OperatorMapper;
import com.antoinevignes.missionmanagement.repository.OperatorRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OperatorServiceTest {

  @Mock private OperatorRepository operatorRepository;

  @Mock private OperatorMapper operatorMapper;

  @InjectMocks private OperatorService operatorService;

  private Operator createOperator(UUID id) {
    return Operator.builder()
        .id(id)
        .firstName("Antoine")
        .lastName("Vignes")
        .licenseNumber("FR-12345")
        .licenseExpiration(LocalDate.parse("2028-10-10"))
        .phone("0608947658")
        .email("antoine.operator@mail.com")
        .status(OperatorStatus.AVAILABLE)
        .createdAt(Instant.parse("2026-01-01T10:00:00Z"))
        .updatedAt(Instant.parse("2026-01-01T10:00:00Z"))
        .build();
  }

  private CreateOperatorRequest createOperatorRequest() {
    return new CreateOperatorRequest(
        "Antoine",
        "Vignes",
        "fr-12345",
        LocalDate.parse("2028-10-10"),
        "0608947658",
        "antoine.operator@mail.com",
        OperatorStatus.AVAILABLE);
  }

  private CreateOperatorRequest updateOperatorRequest() {
    return new CreateOperatorRequest(
        "Jean",
        "Moulin",
        "fr-45678",
        LocalDate.parse("2028-10-12"),
        "0683251792",
        "jean.operator@mail.com",
        OperatorStatus.OFF_DUTY);
  }

  private OperatorResponse createOperatorResponse(Operator operator) {
    return new OperatorResponse(
        operator.getId(),
        operator.getFirstName(),
        operator.getLastName(),
        operator.getLicenseNumber(),
        operator.getLicenseExpiration(),
        operator.getPhone(),
        operator.getEmail(),
        operator.getStatus(),
        operator.getCreatedAt(),
        operator.getUpdatedAt());
  }

  //  CREATE OPERATOR TEST
  @Test
  void shouldCreateOperator() {

    UUID id = UUID.randomUUID();
    CreateOperatorRequest request = createOperatorRequest();
    Operator operator = createOperator(id);
    OperatorResponse expectedResponse = createOperatorResponse(operator);

    when(operatorRepository.existsByLicenseNumber("FR-12345")).thenReturn(false);
    when(operatorMapper.toEntity(request)).thenReturn(operator);
    when(operatorRepository.save(operator)).thenReturn(operator);
    when(operatorMapper.toResponse(operator)).thenReturn(expectedResponse);

    OperatorResponse response = operatorService.createOperator(request);

    assertThat(response).isEqualTo(expectedResponse);

    verify(operatorRepository).existsByLicenseNumber("FR-12345");
    verify(operatorRepository).save(operator);
    verify(operatorMapper).toEntity(request);
    verify(operatorMapper).toResponse(operator);
  }

  //  LICENSE EXCEPTION TEST
  @Test
  void shouldThrowExceptionWhenLicenseAlreadyExists() {

    CreateOperatorRequest request = createOperatorRequest();

    when(operatorRepository.existsByLicenseNumber("FR-12345")).thenReturn(true);

    assertThatThrownBy(() -> operatorService.createOperator(request))
        .isInstanceOf(OperatorAlreadyExistsException.class)
        .hasMessageContaining("FR-12345");

    verify(operatorRepository).existsByLicenseNumber("FR-12345");
    verify(operatorRepository, never()).save(any());
  }

  //  LIST ALL TEST
  @Test
  void shouldListOperators() {

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();
    Operator firstOperator = createOperator(firstId);
    Operator secondOperator = createOperator(secondId);
    OperatorResponse firstResponse = createOperatorResponse(firstOperator);
    OperatorResponse secondResponse = createOperatorResponse(secondOperator);

    when(operatorRepository.findAll()).thenReturn(List.of(firstOperator, secondOperator));
    when(operatorMapper.toResponse(firstOperator)).thenReturn(firstResponse);
    when(operatorMapper.toResponse(secondOperator)).thenReturn(secondResponse);

    List<OperatorResponse> responses = operatorService.listOperators();

    assertThat(responses).containsExactly(firstResponse, secondResponse);

    verify(operatorRepository).findAll();
    verify(operatorMapper).toResponse(firstOperator);
    verify(operatorMapper).toResponse(secondOperator);
  }

  //  GET BY ID TEST
  @Test
  void shouldGetOperatorById() {

    UUID id = UUID.randomUUID();
    Operator operator = createOperator(id);
    OperatorResponse expectedResponse = createOperatorResponse(operator);

    when(operatorRepository.findById(id)).thenReturn(Optional.of(operator));
    when(operatorMapper.toResponse(operator)).thenReturn(expectedResponse);

    OperatorResponse response = operatorService.getOperatorById(id);

    assertThat(response).isEqualTo(expectedResponse);

    verify(operatorRepository).findById(id);
    verify(operatorMapper).toResponse(operator);
  }

  //  OPERATOR NOT FOUND EXCEPTION TEST
  @Test
  void shouldThrowExceptionWhenGettingUnknownOperator() {

    UUID id = UUID.randomUUID();

    when(operatorRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> operatorService.getOperatorById(id))
        .isInstanceOf(OperatorNotFoundException.class)
        .hasMessageContaining(id.toString());

    verify(operatorRepository).findById(id);
    verify(operatorMapper, never()).toResponse(any());
  }

  //  UPDATE OPERATOR TEST
  @Test
  void shouldUpdateOperatorById() {

    UUID id = UUID.randomUUID();
    Operator operator = createOperator(id);
    CreateOperatorRequest request = updateOperatorRequest();
    OperatorResponse expectedResponse = createOperatorResponse(operator);

    when(operatorRepository.findById(id)).thenReturn(Optional.of(operator));
    when(operatorRepository.existsByLicenseNumberAndIdNot("FR-45678", id)).thenReturn(false);
    when(operatorRepository.save(operator)).thenReturn(operator);
    when(operatorMapper.toResponse(operator)).thenReturn(expectedResponse);

    OperatorResponse response = operatorService.updateOperatorById(id, request);

    assertThat(response).isEqualTo(expectedResponse);

    verify(operatorRepository).findById(id);
    verify(operatorRepository).existsByLicenseNumberAndIdNot("FR-45678", id);
    verify(operatorMapper).update(operator, request);
    verify(operatorRepository).save(operator);
    verify(operatorMapper).toResponse(operator);
  }

  //  UPDATE UNKNOWN ID TEST
  @Test
  void shouldThrowExceptionWhenUpdatingUnknownOperator() {

    UUID id = UUID.randomUUID();
    CreateOperatorRequest request = updateOperatorRequest();

    when(operatorRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> operatorService.updateOperatorById(id, request))
        .isInstanceOf(OperatorNotFoundException.class)
        .hasMessageContaining(id.toString());

    verify(operatorRepository, never()).save(any());
    verify(operatorRepository, never()).existsByLicenseNumberAndIdNot(anyString(), any());
  }

  //  LICENSE TAKEN ON UPDATE TEST
  @Test
  void shouldThrowExceptionWhenLicenseBelongsToAnotherOperator() {

    UUID id = UUID.randomUUID();
    Operator operator = createOperator(id);
    CreateOperatorRequest request = updateOperatorRequest();

    when(operatorRepository.findById(id)).thenReturn(Optional.of(operator));
    when(operatorRepository.existsByLicenseNumberAndIdNot("FR-45678", id)).thenReturn(true);

    assertThatThrownBy(() -> operatorService.updateOperatorById(id, request))
        .isInstanceOf(OperatorAlreadyExistsException.class)
        .hasMessageContaining("FR-45678");

    verify(operatorRepository).findById(id);
    verify(operatorRepository).existsByLicenseNumberAndIdNot("FR-45678", id);
    verify(operatorRepository, never()).save(any());
    verify(operatorMapper, never()).update(any(), any());
  }

  //  DELETE BY ID TEST
  @Test
  void shouldDeleteOperatorById() {

    UUID id = UUID.randomUUID();
    Operator operator = createOperator(id);

    when(operatorRepository.findById(id)).thenReturn(Optional.of(operator));

    operatorService.deleteOperatorById(id);

    verify(operatorRepository).findById(id);
    verify(operatorRepository).delete(operator);
  }

  //  DELETE ON UNKNOWN ID TEST
  @Test
  void shouldThrowExceptionWhenDeletingUnknownOperator() {

    UUID id = UUID.randomUUID();

    when(operatorRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> operatorService.deleteOperatorById(id))
        .isInstanceOf(OperatorNotFoundException.class)
        .hasMessageContaining(id.toString());

    verify(operatorRepository, never()).delete(any());
  }
}
