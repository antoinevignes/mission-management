package com.antoinevignes.missionmanagement.controller;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antoinevignes.missionmanagement.dto.aircraft.AircraftResponse;
import com.antoinevignes.missionmanagement.dto.aircraft.CreateAircraftRequest;
import com.antoinevignes.missionmanagement.entity.aircraft.AircraftStatus;
import com.antoinevignes.missionmanagement.entity.aircraft.AircraftType;
import com.antoinevignes.missionmanagement.service.AircraftService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AircraftController.class)
class AircraftControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AircraftService aircraftService;

  @Autowired private ObjectMapper objectMapper;

  private CreateAircraftRequest createAircraftRequest() {
    return new CreateAircraftRequest(
        "f-habc",
        "Gribouille",
        AircraftType.FIXED_WING,
        "Airbus",
        "A320",
        "abc123",
        10,
        BigDecimal.valueOf(9000));
  }

  private AircraftResponse createAircraftResponse(UUID id) {
    return new AircraftResponse(
        id,
        "F-HABC",
        "Gribouille",
        AircraftType.FIXED_WING,
        "Airbus",
        "A320",
        "ABC123",
        AircraftStatus.AVAILABLE,
        10,
        BigDecimal.valueOf(9000),
        null,
        null);
  }

  //  HTTP CREATE 201 TEST
  @Test
  void shouldCreateAircraft() throws Exception {

    UUID id = UUID.randomUUID();
    CreateAircraftRequest request = createAircraftRequest();
    AircraftResponse response = createAircraftResponse(id);

    when(aircraftService.createAircraft(any(CreateAircraftRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/v1/aircrafts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.registration").value("F-HABC"))
        .andExpect(jsonPath("$.name").value("Gribouille"))
        .andExpect(jsonPath("$.serialNumber").value("ABC123"));
  }

  //  HTTP CREATE 400 TEST
  @Test
  void shouldReturnBadRequestWhenCreateAircraftRequestIsInvalid() throws Exception {

    CreateAircraftRequest request =
        new CreateAircraftRequest(
            "",
            "Gribouille",
            AircraftType.FIXED_WING,
            "Airbus",
            "A320",
            "abc123",
            10,
            BigDecimal.valueOf(9000));

    mockMvc
        .perform(
            post("/api/v1/aircrafts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Validation failed."))
        .andExpect(jsonPath("$.errors.registration").value("Registration is required"));

    verifyNoInteractions(aircraftService);
  }

  //  HTTP CREATE 400 TEST MISSING REGISTRATION
  @Test
  void shouldReturnBadRequestWhenRegistrationIsMissing() throws Exception {

    String json =
        """
        {
          "name": "Gribouille",
          "type": "FIXED_WING",
          "manufacturer": "Airbus",
          "model": "A320",
          "serialNumber": "abc123",
          "maxFlightTimeMinutes": 10,
          "maxPayloadKg": 9000
        }
        """;

    mockMvc
        .perform(post("/api/v1/aircrafts").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Validation failed."))
        .andExpect(jsonPath("$.errors", aMapWithSize(1)))
        .andExpect(jsonPath("$.errors.registration").value("Registration is required"));

    verifyNoInteractions(aircraftService);
  }

  //  TEST GET 200
  @Test
  void shouldListAllAircraft() throws Exception {

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    AircraftResponse firstResponse =
        new AircraftResponse(
            firstId,
            "F-HABC",
            "Gribouille",
            AircraftType.FIXED_WING,
            "Airbus",
            "A320",
            "ABC123",
            AircraftStatus.AVAILABLE,
            10,
            BigDecimal.valueOf(9000),
            null,
            null);

    AircraftResponse secondResponse =
        new AircraftResponse(
            secondId,
            "F-HXYZ",
            "Grisouille",
            AircraftType.FIXED_WING,
            "Airbus",
            "A321",
            "XYZ789",
            AircraftStatus.AVAILABLE,
            12,
            BigDecimal.valueOf(9500),
            null,
            null);

    when(aircraftService.listAircraft()).thenReturn(List.of(firstResponse, secondResponse));

    mockMvc
        .perform(get("/api/v1/aircrafts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(firstId.toString()))
        .andExpect(jsonPath("$[0].registration").value("F-HABC"))
        .andExpect(jsonPath("$[0].serialNumber").value("ABC123"))
        .andExpect(jsonPath("$[1].id").value(secondId.toString()))
        .andExpect(jsonPath("$[1].registration").value("F-HXYZ"))
        .andExpect(jsonPath("$[1].serialNumber").value("XYZ789"));
  }
}
