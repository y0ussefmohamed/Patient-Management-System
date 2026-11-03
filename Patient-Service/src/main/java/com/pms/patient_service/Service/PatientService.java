package com.pms.patient_service.Service;

import com.pms.patient_service.DTO.PatientRequestDTO;
import com.pms.patient_service.DTO.PatientResponseDTO;
import com.pms.patient_service.Entity.Patient;
import com.pms.patient_service.Exception.EmailAlreadyExistsException;
import com.pms.patient_service.Exception.PatientNotFoundException;
import com.pms.patient_service.Mapper.PatientMapper;
import com.pms.patient_service.Repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(PatientMapper::toResponseDTO)
                .toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        if (patientRepository.existsByEmail(requestDTO.getEmail()))
            throw new EmailAlreadyExistsException("A patient with this email already exists: " + requestDTO.getEmail());

        Patient patientEntity = PatientMapper.toEntity(requestDTO);
        Patient savedPatient = patientRepository.save(patientEntity);

        return PatientMapper.toResponseDTO(savedPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        // Emails exists and it's not connected to you id
        if(patientRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id))
            throw new EmailAlreadyExistsException("A patient with this email already exists: " + requestDTO.getEmail());

        patient.setName(requestDTO.getName());
        patient.setEmail(requestDTO.getEmail());
        patient.setAddress(requestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(requestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toResponseDTO(updatedPatient);
    }
}
