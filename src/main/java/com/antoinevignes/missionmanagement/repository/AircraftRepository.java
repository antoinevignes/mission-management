package com.antoinevignes.missionmanagement.repository;

import com.antoinevignes.missionmanagement.entity.aircraft.Aircraft;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, UUID> {

  boolean existsByRegistration(String registration);

  boolean existsBySerialNumber(String serialNumber);
}
