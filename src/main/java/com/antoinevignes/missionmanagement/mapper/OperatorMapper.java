package com.antoinevignes.missionmanagement.mapper;

import com.antoinevignes.missionmanagement.dto.operator.CreateOperatorRequest;
import com.antoinevignes.missionmanagement.dto.operator.OperatorResponse;
import com.antoinevignes.missionmanagement.entity.operator.Operator;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class OperatorMapper {

  public Operator toEntity(CreateOperatorRequest request) {

    return Operator.builder()
        .firstName(request.firstName())
        .lastName(request.lastName())
        .licenseNumber(request.licenseNumber().toUpperCase(Locale.ROOT))
        .licenseExpiration(request.licenseExpiration())
        .phone(request.phone())
        .email(request.email())
        .status(request.status())
        .build();
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
