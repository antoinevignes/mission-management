package com.antoinevignes.missionmanagement.exception.operator;

public class OperatorAlreadyExistsException extends RuntimeException {
  public OperatorAlreadyExistsException(String message) {
    super(message);
  }
}
