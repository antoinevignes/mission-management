package com.antoinevignes.missionmanagement.dto.aircraft;

import com.antoinevignes.missionmanagement.entity.aircraft.AircraftType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Locale;

public record CreateAircraftRequest(
    @NotBlank(message = ERROR_REGISTRATION_REQUIRED) String registration,
    @NotBlank(message = ERROR_NAME_REQUIRED) String name,
    @NotNull(message = ERROR_TYPE_REQUIRED) AircraftType type,
    @NotBlank(message = ERROR_MANUFACTURER_REQUIRED) String manufacturer,
    @NotBlank(message = ERROR_MODEL_REQUIRED) String model,
    @NotBlank(message = ERROR_SERIAL_NUMBER_REQUIRED) String serialNumber,
    @NotNull(message = ERROR_MAXIMUM_FLIGHT_TIME_REQUIRED)
        @Positive(message = ERROR_MAXIMUM_FLIGHT_TIME_POSITIVE)
        Integer maxFlightTime,
    @NotNull(message = ERROR_MAXIMUM_PAYLOAD_REQUIRED)
        @Positive(message = ERROR_MAXIMUM_PAYLOAD_POSITIVE)
        BigDecimal maxPayload) {

  public CreateAircraftRequest {
    registration = registration == null ? null : strip(registration.toUpperCase(Locale.ROOT));
    name = strip(name);
    manufacturer = strip(manufacturer);
    model = strip(model);
    serialNumber = serialNumber == null ? null : strip(serialNumber.toUpperCase(Locale.ROOT));
  }

  // Null-safe: a missing field must reach @Valid instead of failing deserialization
  private static String strip(String value) {
    return value == null ? null : value.strip();
  }

  private static final String ERROR_REGISTRATION_REQUIRED = "Registration is required";

  private static final String ERROR_NAME_REQUIRED = "Name is required";

  private static final String ERROR_TYPE_REQUIRED = "Aircraft type is required";

  private static final String ERROR_MANUFACTURER_REQUIRED = "Manufacturer is required";

  private static final String ERROR_MODEL_REQUIRED = "Model is required";

  private static final String ERROR_SERIAL_NUMBER_REQUIRED = "Serial number is required";

  private static final String ERROR_MAXIMUM_FLIGHT_TIME_REQUIRED =
      "Maximum flight time is required";

  private static final String ERROR_MAXIMUM_FLIGHT_TIME_POSITIVE =
      "Maximum flight time must be greater than 0";

  private static final String ERROR_MAXIMUM_PAYLOAD_REQUIRED = "Maximum payload is required";

  private static final String ERROR_MAXIMUM_PAYLOAD_POSITIVE =
      "Maximum payload must be greater than 0";
}
