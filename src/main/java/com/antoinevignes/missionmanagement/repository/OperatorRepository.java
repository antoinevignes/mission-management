package com.antoinevignes.missionmanagement.repository;

import com.antoinevignes.missionmanagement.entity.operator.Operator;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatorRepository extends JpaRepository<Operator, UUID> {

  boolean existsByLicenseNumber(String licenseNumber);

  boolean existsByLicenseNumberAndIdNot(String licenseNumber, UUID id);
}
