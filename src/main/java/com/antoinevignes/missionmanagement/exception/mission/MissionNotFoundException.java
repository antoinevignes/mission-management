package com.antoinevignes.missionmanagement.exception.mission;

public class MissionNotFoundException extends RuntimeException {
  public MissionNotFoundException(String message) {
    super(message);
  }
}
