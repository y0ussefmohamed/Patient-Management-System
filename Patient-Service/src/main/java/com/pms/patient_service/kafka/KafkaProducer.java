package com.pms.patient_service.kafka;

import com.pms.patient_service.Entity.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try {
            // Return successfully only after Kafka acknowledges the event.
            kafkaTemplate.send("patient", event.toByteArray()).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaException("Patient event delivery was interrupted", e);
        } catch (Exception e) {
            log.error("Error sending PatientCreated event: {}", event, e);
            throw new KafkaException("Patient event could not be published", e);
        }
    }
}
