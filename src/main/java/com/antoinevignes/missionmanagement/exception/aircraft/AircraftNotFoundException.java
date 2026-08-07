package com.antoinevignes.missionmanagement.exception.aircraft;

public class AircraftNotFoundException extends RuntimeException {
  public AircraftNotFoundException(String message) {
    super(message);
  }
}
