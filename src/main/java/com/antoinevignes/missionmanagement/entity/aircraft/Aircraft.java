package com.antoinevignes.missionmanagement.entity.aircraft;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
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

  @Column(name = "max_flight_time_minutes", nullable = false)
  private Integer maxFlightTimeMinutes;

  @Column(name = "max_payload_kg", nullable = false, precision = 8, scale = 2)
  private BigDecimal maxPayloadKg;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
