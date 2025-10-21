package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {
  public PatientNotFoundException(String phoneNumber) {
    super("Pacjent o numerze telefonu " + phoneNumber + " nie został znaleziony.");
  }
}
