package com.pms.patient_service.Repository;

import com.pms.patient_service.Entity.Patient;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByEmail(String email);

    // To validate if another user entered this email before
    boolean existsByEmailAndIdNot(String email, UUID id);
}
