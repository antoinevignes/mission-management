package com.antoinevignes.missionmanagement.dto.mission;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

public record CreateMissionRequest(
    @NotBlank(message = ERROR_CODE_REQUIRED) String code,
    @NotBlank(message = ERROR_TITLE_REQUIRED) String title,
    @NotNull(message = ERROR_AIRCRAFT_REQUIRED) UUID aircraftId,
    @NotNull(message = ERROR_OPERATOR_REQUIRED) UUID operatorId,
    @NotBlank(message = ERROR_DEPARTURE_AIRPORT_REQUIRED)
        @Pattern(message = ERROR_AIRPORT_ICAO_FORMAT, regexp = ICAO_AIRPORT_REGEX)
        String departureAirport,
    @NotBlank(message = ERROR_ARRIVAL_AIRPORT_REQUIRED)
        @Pattern(message = ERROR_AIRPORT_ICAO_FORMAT, regexp = ICAO_AIRPORT_REGEX)
        String arrivalAirport,
    @NotNull(message = ERROR_PLANNED_START_REQUIRED) Instant plannedStart,
    @NotNull(message = ERROR_PLANNED_END_REQUIRED) Instant plannedEnd,
    @NotNull(message = ERROR_PAYLOAD_KG_REQUIRED)
        @PositiveOrZero(message = ERROR_PAYLOAD_POSITIVE_OR_ZERO)
        BigDecimal payloadKg) {

  public CreateMissionRequest {
    code = code == null ? null : strip(code.toUpperCase(Locale.ROOT));
    title = strip(title);
    departureAirport =
        departureAirport == null ? null : strip(departureAirport.toUpperCase(Locale.ROOT));
    arrivalAirport = arrivalAirport == null ? null : strip(arrivalAirport.toUpperCase(Locale.ROOT));
  }

  private static String strip(String value) {
    return value == null ? null : value.strip();
  }

  @AssertTrue(message = ERROR_PLANNED_SLOT_INVALID)
  public boolean isPlannedEndAfterStart() {
    if (plannedStart == null || plannedEnd == null) {
      return true;
    }
    return plannedStart.isBefore(plannedEnd);
  }

  private static final String ICAO_AIRPORT_REGEX = "^[A-Z]{4}$";

  private static final String ERROR_CODE_REQUIRED = "Mission code is required";

  private static final String ERROR_TITLE_REQUIRED = "Mission title is required";

  private static final String ERROR_AIRCRAFT_REQUIRED = "Aircraft ID is required";

  private static final String ERROR_OPERATOR_REQUIRED = "Operator ID is required";

  private static final String ERROR_DEPARTURE_AIRPORT_REQUIRED = "Departure Airport is required";

  private static final String ERROR_ARRIVAL_AIRPORT_REQUIRED = "Arrival Airport is required";

  private static final String ERROR_AIRPORT_ICAO_FORMAT = "Airport must be a 4-letter ICAO code";

  private static final String ERROR_PLANNED_START_REQUIRED = "Planned start is required";

  private static final String ERROR_PLANNED_END_REQUIRED = "Planned end is required";

  private static final String ERROR_PLANNED_SLOT_INVALID =
      "Planned end must be after planned start";

  private static final String ERROR_PAYLOAD_KG_REQUIRED = "Payload is required";

  private static final String ERROR_PAYLOAD_POSITIVE_OR_ZERO = "Payload must not be negative";
}
