package com.antoinevignes.missionmanagement.entity.aircraft;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aircraft {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String registration;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AircraftType type;

  @Column(nullable = false)
  private String manufacturer;

  @Column(nullable = false)
  private String model;

  @Column(name = "serial_number", nullable = false, unique = true)
  private String serialNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AircraftStatus status;

  @Column(name = "max_flight_time", nullable = false)
  private Integer maxFlightTime;

  @Column(name = "max_payload", nullable = false)
  private BigDecimal maxPayload;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
