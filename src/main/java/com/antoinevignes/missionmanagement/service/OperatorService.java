package com.antoinevignes.missionmanagement.service;

import com.antoinevignes.missionmanagement.dto.operator.CreateOperatorRequest;
import com.antoinevignes.missionmanagement.dto.operator.OperatorResponse;
import com.antoinevignes.missionmanagement.dto.operator.UpdateOperatorRequest;
import com.antoinevignes.missionmanagement.entity.operator.Operator;
import com.antoinevignes.missionmanagement.exception.operator.OperatorAlreadyExistsException;
import com.antoinevignes.missionmanagement.exception.operator.OperatorNotFoundException;
import com.antoinevignes.missionmanagement.mapper.OperatorMapper;
import com.antoinevignes.missionmanagement.repository.OperatorRepository;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperatorService {

  private final OperatorRepository operatorRepository;
  private final OperatorMapper operatorMapper;

  //  CREATE OPERATOR
  public OperatorResponse createOperator(CreateOperatorRequest request) {

    String licenseNumber = request.licenseNumber().toUpperCase(Locale.ROOT);

    if (operatorRepository.existsByLicenseNumber(licenseNumber)) {
      throw new OperatorAlreadyExistsException(
          "Operator with license number '" + licenseNumber + "' already exists.");
    }

    Operator operator = operatorMapper.toEntity(request);

    Operator savedOperator = operatorRepository.save(operator);

    return operatorMapper.toResponse(savedOperator);
  }

  //  LIST ALL OPERATORS
  public List<OperatorResponse> listOperators() {

    List<Operator> operators = operatorRepository.findAll();

    return operators.stream().map(operatorMapper::toResponse).toList();
  }

  //  GET OPERATOR BY ID
  public OperatorResponse getOperatorById(UUID id) {

    Operator operator =
        operatorRepository
            .findById(id)
            .orElseThrow(
                () -> new OperatorNotFoundException("Operator with id '" + id + "' not found."));

    return operatorMapper.toResponse(operator);
  }

  //  UPDATE OPERATOR
  public OperatorResponse updateOperatorById(UUID id, UpdateOperatorRequest request) {

    String licenseNumber = request.licenseNumber().toUpperCase(Locale.ROOT);

    Operator operator =
        operatorRepository
            .findById(id)
            .orElseThrow(
                () -> new OperatorNotFoundException("Operator with id '" + id + "' not found."));

    if (operatorRepository.existsByLicenseNumberAndIdNot(licenseNumber, id)) {
      throw new OperatorAlreadyExistsException(
          "Operator with license number '" + licenseNumber + "' already exists.");
    }

    operator.setFirstName(request.firstName());
    operator.setLastName(request.lastName());
    operator.setLicenseNumber(licenseNumber);
    operator.setLicenseExpiration(request.licenseExpiration());
    operator.setPhone(request.phone());
    operator.setEmail(request.email());
    operator.setStatus(request.status());

    Operator savedOperator = operatorRepository.save(operator);

    return operatorMapper.toResponse(savedOperator);
  }

  //  DELETE OPERATOR
  public void deleteOperatorById(UUID id) {
    Operator operator =
        operatorRepository
            .findById(id)
            .orElseThrow(
                () -> new OperatorNotFoundException("Operator with id '" + id + "' not found."));

    operatorRepository.delete(operator);
  }
}
