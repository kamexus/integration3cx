package com.example.service;

import com.example.exception.PatientNotFoundException;
import com.example.model.Patient;
import com.example.model.PatientDTO;
import com.example.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientDTO findPatientByPhoneNumber(String phoneNumber) {
        String raw = phoneNumber == null ? "" : phoneNumber.trim();
        String normalized = normalizePhonePl(raw);

        log.debug("Searching patient. raw='{}', normalized='{}'", raw, normalized);

        Patient patient = patientRepository.findByPhoneNumber(normalized)
                .orElseGet(() -> patientRepository.findByPhoneNumber(raw)
                        .orElseThrow(() -> new PatientNotFoundException(raw)));

        return new PatientDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhoneNumber()
        );
    }

    private String normalizePhonePl(String input) {
        if (input == null || input.isBlank()) return "";
        String digits = input.replaceAll("\\D+", "");
        if (digits.isBlank()) return "";

        if (digits.startsWith("0048") && digits.length() == 13) {
            return "+" + digits.substring(2);
        }
        if (digits.length() == 9) {
            return "+48" + digits;
        }
        if (digits.length() == 10 && digits.startsWith("0")) {
            return "+48" + digits.substring(1);
        }
        if (digits.length() == 11 && digits.startsWith("48")) {
            return "+" + digits;
        }
        if (digits.startsWith("00") && digits.length() > 2) {
            return "+" + digits.substring(2);
        }
        return "+" + digits;
    }

}
