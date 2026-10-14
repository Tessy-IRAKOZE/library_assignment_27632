package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public List<Patient> findByLastNameIgnoreCase(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Patient> findPatientsByDoctorId(UUID doctorId) {
        if (!doctorRepo.existsById(doctorId)) {
            return null;
        }
        return patientRepo.findPatientsByDoctorId(doctorId);
    }

    public List<Patient> findFrequentPatients(int min) {
        return patientRepo.findFrequentPatients(min);
    }

    public List<Appointment> findUpcoming(String ssNumber) {
        if (!patientRepo.existsBySsNumber(ssNumber)) {
            return null;
        }
        List<AppointmentStatus> statuses = new ArrayList<>();
        statuses.add(AppointmentStatus.PENDING);
        statuses.add(AppointmentStatus.CONFIRMED);
        return appointmentRepo.findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(ssNumber, LocalDate.now(), statuses);
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
