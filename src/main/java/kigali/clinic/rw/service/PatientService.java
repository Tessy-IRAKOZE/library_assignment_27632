package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    public List<Patient> findByLastNameIgnoreCase(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public String savePatient(Patient patient) {
        patientRepo.save(patient);
        return "Patient saved successfully";
    }

    public List<Patient> findAll() {
        return patientRepo.findAll();
    }

    public Optional<Patient> findById(UUID id) {
        return patientRepo.findById(id);
    }

    public String updatePatient(UUID id, Patient updatedPatient) {
        Optional<Patient> existing = patientRepo.findById(id);
        if (existing.isEmpty()) {
            return "Patient not found";
        }
        Patient patient = existing.get();
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setDateOfBirth(updatedPatient.getDateOfBirth());
        patientRepo.save(patient);
        return "Patient updated successfully";
    }

    public String deletePatient(UUID id) {
        if (!patientRepo.existsById(id)) {
            return "Patient not found";
        }
        patientRepo.deleteById(id);
        return "Patient deleted successfully";
    }
}
