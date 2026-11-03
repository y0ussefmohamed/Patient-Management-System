package com.pms.patient_service.Service;

import com.pms.patient_service.DTO.PatientRequestDTO;
import com.pms.patient_service.DTO.PatientResponseDTO;
import com.pms.patient_service.Entity.Patient;
import com.pms.patient_service.Mapper.PatientMapper;
import com.pms.patient_service.Repository.PatientRepository;
import java.util.List;
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
        Patient patientEntity = PatientMapper.toEntity(requestDTO);
        return PatientMapper.toResponseDTO(patientRepository.save(patientEntity));
    }
}
