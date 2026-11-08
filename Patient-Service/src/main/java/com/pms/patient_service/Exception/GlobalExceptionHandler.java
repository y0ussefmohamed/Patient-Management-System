package com.pms.patient_service.Exception;

import io.grpc.StatusRuntimeException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDate() {
        return ResponseEntity.badRequest().body(
                Map.of("dateOfBirth", "Date of birth must be a valid date in yyyy-MM-dd format"));
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<Map<String, String>> handleBillingFailure() {
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                "Billing service is unavailable. Please try again.");
    }

    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<Map<String, String>> handleKafkaFailure() {
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE,
                "Patient event could not be published. Please try again.");
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}
