package com.pms.patient_service.Mapper;

import com.pms.patient_service.DTO.PatientResponseDTO;
import com.pms.patient_service.Entity.Patient;

public class PatientMapper {
    public static PatientResponseDTO toResponseDTO(Patient patient) {
        if (patient == null)
            return null;

        return PatientResponseDTO.builder()
                .id(patient.getId() != null ? patient.getId().toString() : null)
                .name(patient.getName())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : null)
                .build();
    }
}
