package com.antoinevignes.missionmanagement.dto.operator;

import com.antoinevignes.missionmanagement.entity.operator.OperatorStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Locale;

public record CreateOperatorRequest(
    @NotBlank(message = ERROR_FIRST_NAME_REQUIRED) String firstName,
    @NotBlank(message = ERROR_LAST_NAME_REQUIRED) String lastName,
    @NotBlank(message = ERROR_LICENSE_REQUIRED) String licenseNumber,
    @NotNull(message = ERROR_LICENSE_EXPIRATION_REQUIRED) LocalDate licenseExpiration,
    String phone,
    @NotBlank(message = ERROR_EMAIL_REQUIRED) @Email(message = ERROR_EMAIL_INVALID) String email,
    @NotNull(message = ERROR_STATUS_REQUIRED) OperatorStatus status) {

  public CreateOperatorRequest {
    firstName = firstName.strip();
    lastName = lastName.strip();
    licenseNumber = licenseNumber.toUpperCase(Locale.ROOT).strip();
    phone = phone == null ? null : phone.strip();
    email = email.strip();
  }

  private static final String ERROR_FIRST_NAME_REQUIRED = "First name is required";

  private static final String ERROR_LAST_NAME_REQUIRED = "Last name is required";

  private static final String ERROR_LICENSE_REQUIRED = "License is required";

  private static final String ERROR_LICENSE_EXPIRATION_REQUIRED = "License expiration is required";

  private static final String ERROR_EMAIL_REQUIRED = "Email is required";

  private static final String ERROR_STATUS_REQUIRED = "Operator status is required";

  private static final String ERROR_EMAIL_INVALID = "Email must be valid";
}