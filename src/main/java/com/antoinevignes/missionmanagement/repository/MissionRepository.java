package com.antoinevignes.missionmanagement.repository;

import com.antoinevignes.missionmanagement.entity.mission.Mission;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, UUID> {

  boolean existsByCode(String code);

  boolean existsByCodeAndIdNot(String code, UUID id);
}
