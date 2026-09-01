package com.antoinevignes.missionmanagement.entity.operator;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operator {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, name = "first_name")
  private String firstName;

  @Column(nullable = false, name = "last_name")
  private String lastName;

  @Column(nullable = false, name = "license_number", unique = true)
  private String licenseNumber;

  @Column(nullable = false, name = "license_expiration")
  private LocalDate licenseExpiration;

  @Column private String phone;

  @Column(nullable = false)
  private String email;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private OperatorStatus status;
}
