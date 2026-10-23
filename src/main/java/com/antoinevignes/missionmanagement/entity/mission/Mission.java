package com.antoinevignes.missionmanagement.entity.mission;

import com.antoinevignes.missionmanagement.entity.aircraft.Aircraft;
import com.antoinevignes.missionmanagement.entity.operator.Operator;
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
public class Mission {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String title;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "aircraft_id", nullable = false)
  private Aircraft aircraft;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "operator_id", nullable = false)
  private Operator operator;

  @Column(name = "departure_airport", nullable = false, length = 4)
  private String departureAirport;

  @Column(name = "arrival_airport", nullable = false, length = 4)
  private String arrivalAirport;

  @Column(name = "planned_start", nullable = false)
  private Instant plannedStart;

  @Column(name = "planned_end", nullable = false)
  private Instant plannedEnd;

  @Column(name = "payload_kg", nullable = false, precision = 8, scale = 2)
  private BigDecimal payloadKg;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MissionStatus status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;
}
