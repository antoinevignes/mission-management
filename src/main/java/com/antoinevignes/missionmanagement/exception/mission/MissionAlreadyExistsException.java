package com.antoinevignes.missionmanagement.exception.mission;

public class MissionAlreadyExistsException extends RuntimeException {
  public MissionAlreadyExistsException(String message) {
    super(message);
  }
}
