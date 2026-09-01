package com.antoinevignes.missionmanagement.mapper;

import com.antoinevignes.missionmanagement.dto.operator.CreateOperatorRequest;
import com.antoinevignes.missionmanagement.dto.operator.OperatorResponse;
import com.antoinevignes.missionmanagement.entity.operator.Operator;
import org.springframework.stereotype.Component;

@Component
public class OperatorMapper {

  public Operator toEntity(CreateOperatorRequest request) {

    return Operator.builder()
        .firstName(request.firstName())
        .lastName(request.lastName())
        .licenseNumber(request.licenseNumber())
        .licenseExpiration(request.licenseExpiration())
        .phone(request.phone())
        .email(request.email())
        .status(request.status())
        .build();
  }

  public Operator update(Operator operator, CreateOperatorRequest request) {

    operator.setFirstName(request.firstName());
    operator.setLastName(request.lastName());
    operator.setLicenseNumber(request.licenseNumber());
    operator.setLicenseExpiration(request.licenseExpiration());
    operator.setPhone(request.phone());
    operator.setEmail(request.email());
    operator.setStatus(request.status());

    return operator;
  }

  public OperatorResponse toResponse(Operator operator) {

    return new OperatorResponse(
        operator.getId(),
        operator.getFirstName(),
        operator.getLastName(),
        operator.getLicenseNumber(),
        operator.getLicenseExpiration(),
        operator.getPhone(),
        operator.getEmail(),
        operator.getStatus());
  }
}
