package com.antoinevignes.missionmanagement.exception.aircraft;

public class AircraftAlreadyExistsException extends RuntimeException {
  public AircraftAlreadyExistsException(String message) {
    super(message);
  }
}
