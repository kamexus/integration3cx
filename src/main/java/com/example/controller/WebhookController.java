package com.example.controller;

import com.example.model.PatientDTO;
import com.example.service.PatientService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/webhook", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Validated
@RequiredArgsConstructor
public class WebhookController {

    private final PatientService patientService;

    @GetMapping("/3cx")
    public ResponseEntity<PatientDTO> handle3cxWebhookGet(@RequestParam("phoneNumber") @NotBlank String phoneNumber) {
        log.info("[GET] Webhook 3CX phoneNumber=***{}", last4(phoneNumber));
        return ResponseEntity.ok(patientService.findPatientByPhoneNumber(phoneNumber));
    }

    @PostMapping(value = "/3cx", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<PatientDTO> handle3cxWebhookForm(@RequestParam("phoneNumber") @NotBlank String phoneNumber) {
        log.info("[POST-FORM] Webhook 3CX phoneNumber=***{}", last4(phoneNumber));
        return ResponseEntity.ok(patientService.findPatientByPhoneNumber(phoneNumber));
    }

    @PostMapping(value = "/3cx", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> handle3cxWebhookJson(@RequestBody Map<String, Object> payload) {
        String phoneNumber = null;
        for (String key : new String[]{"phoneNumber", "callerid", "Caller", "caller", "from", "From"}) {
            Object v = payload.get(key);
            if (v != null) { phoneNumber = String.valueOf(v).trim(); break; }
        }
        log.info("[POST-JSON] Webhook 3CX keys={}, phoneNumber=***{}", payload.keySet(), phoneNumber == null ? "" : last4(phoneNumber));
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(patientService.findPatientByPhoneNumber(phoneNumber));
    }

    private String last4(String num) {
        if (num == null || num.length() <= 4) return num == null ? "" : num;
        return num.substring(num.length() - 4);
    }
}