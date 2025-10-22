package com.example.controller;

import com.example.model.PatientDTO;
import com.example.service.PatientService;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/webhook", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Validated
@RequiredArgsConstructor
public class WebhookController {

    private final PatientService patientService;

    @GetMapping("/3cx")
    public ResponseEntity<PatientDTO> handle3cxWebhookGet(@RequestParam("phoneNumber") @NotBlank String phoneNumber) {
        log.info("[GET] 3CX webhook phone={}", phoneNumber);
        return ResponseEntity.ok(patientService.findPatientByPhoneNumber(phoneNumber));
    }

    @PostMapping(value = "/3cx", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<PatientDTO> handle3cxWebhookForm(@RequestParam("phoneNumber") @NotBlank String phoneNumber) {
        log.info("[POST-FORM] 3CX webhook phone={}", phoneNumber);
        return ResponseEntity.ok(patientService.findPatientByPhoneNumber(phoneNumber));
    }

    @PostMapping(value = "/3cx", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> handle3cxWebhookJson(@RequestBody @Validated WebhookRequest req) {
        String phoneNumber = req.phoneNumber();
        log.info("[POST-JSON] 3CX webhook phone={}", phoneNumber);
        return ResponseEntity.ok(patientService.findPatientByPhoneNumber(phoneNumber));
    }
}

record WebhookRequest(
        @JsonAlias({"phoneNumber", "phonenumber", "callerid", "caller", "caller_id", "callerId", "from", "From", "Caller"})
        @NotBlank
        String phoneNumber
) {}