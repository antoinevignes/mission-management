package com.antoinevignes.missionmanagement.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.antoinevignes.missionmanagement.dto.operator.CreateOperatorRequest;
import com.antoinevignes.missionmanagement.dto.operator.OperatorResponse;
import com.antoinevignes.missionmanagement.entity.operator.OperatorStatus;
import com.antoinevignes.missionmanagement.exception.operator.OperatorAlreadyExistsException;
import com.antoinevignes.missionmanagement.exception.operator.OperatorNotFoundException;
import com.antoinevignes.missionmanagement.service.OperatorService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(OperatorController.class)
class OperatorControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private OperatorService operatorService;

  @Autowired private ObjectMapper objectMapper;

  private CreateOperatorRequest createOperatorRequest() {
    return new CreateOperatorRequest(
        "Antoine",
        "Vignes",
        "fr-12345",
        LocalDate.parse("2028-10-10"),
        "0608947658",
        "antoine.operator@mail.com",
        OperatorStatus.AVAILABLE);
  }

  private OperatorResponse createOperatorResponse(UUID id) {
    return new OperatorResponse(
        id,
        "Antoine",
        "Vignes",
        "FR-12345",
        LocalDate.parse("2028-10-10"),
        "0608947658",
        "antoine.operator@mail.com",
        OperatorStatus.AVAILABLE,
        null,
        null);
  }

  private CreateOperatorRequest updateOperatorRequest() {
    return new CreateOperatorRequest(
        "Jean",
        "Moulin",
        "fr-45678",
        LocalDate.parse("2028-10-12"),
        "0683251792",
        "jean.operator@mail.com",
        OperatorStatus.OFF_DUTY);
  }

  private OperatorResponse updateOperatorResponse(UUID id) {
    return new OperatorResponse(
        id,
        "Jean",
        "Moulin",
        "FR-45678",
        LocalDate.parse("2028-10-12"),
        "0683251792",
        "jean.operator@mail.com",
        OperatorStatus.OFF_DUTY,
        null,
        null);
  }

  //  HTTP CREATE 201 TEST
  @Test
  void shouldCreateOperator() throws Exception {

    UUID id = UUID.randomUUID();
    CreateOperatorRequest request = createOperatorRequest();
    OperatorResponse response = createOperatorResponse(id);

    when(operatorService.createOperator(any(CreateOperatorRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/v1/operators")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.firstName").value("Antoine"))
        .andExpect(jsonPath("$.lastName").value("Vignes"))
        .andExpect(jsonPath("$.licenseNumber").value("FR-12345"))
        .andExpect(jsonPath("$.licenseExpiration").value("2028-10-10"))
        .andExpect(jsonPath("$.phone").value("0608947658"))
        .andExpect(jsonPath("$.email").value("antoine.operator@mail.com"))
        .andExpect(jsonPath("$.status").value("AVAILABLE"));

    verify(operatorService).createOperator(request);
  }

  //  HTTP GET LIST 200 TEST
  @Test
  void shouldListOperators() throws Exception {

    UUID firstId = UUID.randomUUID();
    UUID secondId = UUID.randomUUID();

    OperatorResponse firstResponse =
        new OperatorResponse(
            firstId,
            "Antoine",
            "Vignes",
            "FR-12345",
            LocalDate.parse("2028-10-10"),
            "0608947658",
            "antoine.operator@mail.com",
            OperatorStatus.AVAILABLE,
            null,
            null);

    OperatorResponse secondResponse =
        new OperatorResponse(
            secondId,
            "Michel",
            "Blanc",
            "FR-45678",
            LocalDate.parse("2028-10-12"),
            "0761617534",
            "michel.operator@mail.com",
            OperatorStatus.AVAILABLE,
            null,
            null);

    when(operatorService.listOperators()).thenReturn(List.of(firstResponse, secondResponse));

    mockMvc
        .perform(get("/api/v1/operators"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$.[0].id").value(firstId.toString()))
        .andExpect(jsonPath("$.[0].firstName").value("Antoine"))
        .andExpect(jsonPath("$.[0].lastName").value("Vignes"))
        .andExpect(jsonPath("$.[0].licenseNumber").value("FR-12345"))
        .andExpect(jsonPath("$.[0].licenseExpiration").value("2028-10-10"))
        .andExpect(jsonPath("$.[0].phone").value("0608947658"))
        .andExpect(jsonPath("$.[0].status").value("AVAILABLE"))
        .andExpect(jsonPath("$.[1].id").value(secondId.toString()))
        .andExpect(jsonPath("$.[1].firstName").value("Michel"))
        .andExpect(jsonPath("$.[1].lastName").value("Blanc"))
        .andExpect(jsonPath("$.[1].licenseNumber").value("FR-45678"))
        .andExpect(jsonPath("$.[1].licenseExpiration").value("2028-10-12"))
        .andExpect(jsonPath("$.[1].phone").value("0761617534"))
        .andExpect(jsonPath("$.[1].status").value("AVAILABLE"));

    verify(operatorService).listOperators();
  }

  //  HTTP GET BY ID 200
  @Test
  void shouldGetOperatorById() throws Exception {

    UUID id = UUID.randomUUID();

    OperatorResponse response = createOperatorResponse(id);

    when(operatorService.getOperatorById(id)).thenReturn(response);

    mockMvc
        .perform(get("/api/v1/operators/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.firstName").value("Antoine"))
        .andExpect(jsonPath("$.lastName").value("Vignes"))
        .andExpect(jsonPath("$.licenseNumber").value("FR-12345"))
        .andExpect(jsonPath("$.licenseExpiration").value("2028-10-10"))
        .andExpect(jsonPath("$.phone").value("0608947658"))
        .andExpect(jsonPath("$.email").value("antoine.operator@mail.com"))
        .andExpect(jsonPath("$.status").value("AVAILABLE"));

    verify(operatorService).getOperatorById(id);
  }

  //  HTTP GET BY ID 404
  @Test
  void shouldReturnNotFoundWhenOperatorDoesNotExist() throws Exception {

    UUID id = UUID.randomUUID();

    when(operatorService.getOperatorById(id))
        .thenThrow(new OperatorNotFoundException("Operator with id '" + id + "' not found."));

    mockMvc
        .perform(get("/api/v1/operators/" + id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value(containsString(id.toString())))
        .andExpect(jsonPath("$.path").value("/api/v1/operators/" + id));

    verify(operatorService).getOperatorById(id);
  }

  //  HTTP PUT 200 TEST
  @Test
  void shouldUpdateOperator() throws Exception {

    UUID id = UUID.randomUUID();
    CreateOperatorRequest request = updateOperatorRequest();
    OperatorResponse response = updateOperatorResponse(id);

    when(operatorService.updateOperatorById(eq(id), any(CreateOperatorRequest.class)))
        .thenReturn(response);

    mockMvc
        .perform(
            put("/api/v1/operators/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.firstName").value("Jean"))
        .andExpect(jsonPath("$.lastName").value("Moulin"))
        .andExpect(jsonPath("$.licenseNumber").value("FR-45678"))
        .andExpect(jsonPath("$.licenseExpiration").value("2028-10-12"))
        .andExpect(jsonPath("$.phone").value("0683251792"))
        .andExpect(jsonPath("$.email").value("jean.operator@mail.com"))
        .andExpect(jsonPath("$.status").value("OFF_DUTY"));

    verify(operatorService).updateOperatorById(id, request);
  }

  //  HTTP PUT 404 TEST
  @Test
  void shouldReturnNotFoundWhenUpdatingUnknownOperator() throws Exception {

    UUID id = UUID.randomUUID();
    CreateOperatorRequest request = updateOperatorRequest();

    when(operatorService.updateOperatorById(eq(id), any(CreateOperatorRequest.class)))
        .thenThrow(new OperatorNotFoundException("Operator with id '" + id + "' not found."));

    mockMvc
        .perform(
            put("/api/v1/operators/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value(containsString(id.toString())))
        .andExpect(jsonPath("$.path").value("/api/v1/operators/" + id));

    verify(operatorService).updateOperatorById(id, request);
  }

  //  HTTP PUT 409 TEST
  @Test
  void shouldReturnConflictWhenLicenseBelongsToAnotherOperator() throws Exception {

    UUID id = UUID.randomUUID();
    CreateOperatorRequest request = updateOperatorRequest();

    when(operatorService.updateOperatorById(eq(id), any(CreateOperatorRequest.class)))
        .thenThrow(
            new OperatorAlreadyExistsException(
                "Operator with license number 'FR-45678' already exists."));

    mockMvc
        .perform(
            put("/api/v1/operators/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.error").value("Conflict"))
        .andExpect(jsonPath("$.message").value(containsString("FR-45678")))
        .andExpect(jsonPath("$.path").value("/api/v1/operators/" + id));

    verify(operatorService).updateOperatorById(id, request);
  }

  //  HTTP DELETE 204 TEST
  @Test
  void shouldDeleteOperator() throws Exception {

    UUID id = UUID.randomUUID();

    mockMvc.perform(delete("/api/v1/operators/" + id)).andExpect(status().isNoContent());

    verify(operatorService).deleteOperatorById(id);
  }

  //  HTTP DELETE 404 TEST
  @Test
  void shouldReturnNotFoundWhenDeletingUnknownOperator() throws Exception {
    UUID id = UUID.randomUUID();

    doThrow(new OperatorNotFoundException("Operator with id '" + id + "' not found."))
        .when(operatorService)
        .deleteOperatorById(id);

    mockMvc
        .perform(delete("/api/v1/operators/" + id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(jsonPath("$.message").value(containsString(id.toString())))
        .andExpect(jsonPath("$.path").value("/api/v1/operators/" + id));

    verify(operatorService).deleteOperatorById(id);
  }

  //  HTTP CREATE 400 TEST INVALID EMAIL
  @Test
  void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {

    CreateOperatorRequest request =
        new CreateOperatorRequest(
            "Antoine",
            "Vignes",
            "FR-12345",
            LocalDate.parse("2028-10-10"),
            "0608947658",
            "saodkoa",
            OperatorStatus.AVAILABLE);

    mockMvc
        .perform(
            post("/api/v1/operators")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Validation failed."))
        .andExpect(jsonPath("$.errors.email").value("Email must be valid"));

    verifyNoInteractions(operatorService);
  }

  //  HTTP CREATE 400 TEST REQUIRED EMAIL
  @Test
  void shouldReturnBadRequestWhenEmailIsBlank() throws Exception {

    CreateOperatorRequest request =
        new CreateOperatorRequest(
            "Antoine",
            "Vignes",
            "FR-12345",
            LocalDate.parse("2028-10-10"),
            "0608947658",
            "",
            OperatorStatus.AVAILABLE);

    mockMvc
        .perform(
            post("/api/v1/operators")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Validation failed."))
        .andExpect(jsonPath("$.errors.email").value("Email is required"));

    verifyNoInteractions(operatorService);
  }

  //  HTTP CREATE 400 TEST BLANK FIRST NAME
  @Test
  void shouldReturnBadRequestWhenFirstNameIsBlank() throws Exception {

    String json =
        """
        {
          "firstName": "",
          "lastName": "Vignes",
          "licenseNumber": "FR-12345",
          "licenseExpiration": "2028-10-10",
          "email": "antoine.operator@mail.com",
          "status": "AVAILABLE"
        }
        """;

    mockMvc
        .perform(post("/api/v1/operators").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Validation failed."))
        .andExpect(jsonPath("$.errors.firstName").value("First name is required"));

    verifyNoInteractions(operatorService);
  }

  //  HTTP CREATE 400 TEST MISSING FIRST NAME
  @Test
  void shouldReturnBadRequestWhenFirstNameIsMissing() throws Exception {

    String json =
        """
        {
          "lastName": "Vignes",
          "licenseNumber": "FR-12345",
          "licenseExpiration": "2028-10-10",
          "email": "antoine.operator@mail.com",
          "status": "AVAILABLE"
        }
        """;

    mockMvc
        .perform(post("/api/v1/operators").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value("Validation failed."))
        .andExpect(jsonPath("$.errors.firstName").value("First name is required"));

    verifyNoInteractions(operatorService);
  }
}
