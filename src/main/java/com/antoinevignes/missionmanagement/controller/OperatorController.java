package com.antoinevignes.missionmanagement.controller;

import com.antoinevignes.missionmanagement.dto.operator.CreateOperatorRequest;
import com.antoinevignes.missionmanagement.dto.operator.OperatorResponse;
import com.antoinevignes.missionmanagement.service.OperatorService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/operators")
@RequiredArgsConstructor
public class OperatorController {

  private final OperatorService operatorService;

  @PostMapping
  public ResponseEntity<OperatorResponse> createOperator(
      @Valid @RequestBody CreateOperatorRequest request) {

    OperatorResponse response = operatorService.createOperator(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<OperatorResponse>> listOperators() {

    return ResponseEntity.ok(operatorService.listOperators());
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<OperatorResponse> getOperatorById(@PathVariable UUID id) {

    return ResponseEntity.ok(operatorService.getOperatorById(id));
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<OperatorResponse> updateOperatorById(
      @PathVariable UUID id, @Valid @RequestBody CreateOperatorRequest request) {

    OperatorResponse response = operatorService.updateOperatorById(id, request);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> deleteOperatorById(@PathVariable UUID id) {

    operatorService.deleteOperatorById(id);

    return ResponseEntity.noContent().build();
  }
}
