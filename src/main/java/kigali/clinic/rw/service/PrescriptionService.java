package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String savePrescription(UUID appointmentId, Prescription prescription) {
        Optional<Appointment> foundAppointment = appointmentRepo.findById(appointmentId);
        if (foundAppointment.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = foundAppointment.get();
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            prescription.setIssuedDate(LocalDate.now());
            prescription.setAppointment(appointment);
            prescriptionRepo.save(prescription);
            return "Prescription saved successfully";
        } else {
            return "Prescriptions are only for COMPLETED appointments";
        }
    }

    public List<Prescription> findByAppointment(UUID appointmentId) {
        Optional<Appointment> foundAppointment = appointmentRepo.findById(appointmentId);
        if (foundAppointment.isEmpty()) {
            return null;
        }
        return prescriptionRepo.findByAppointment(foundAppointment.get());
    }

    public List<Prescription> findByPatientSsNumber(String ssNumber) {
        return prescriptionRepo.findByPatientSsNumber(ssNumber);
    }

    public List<Object[]> findTopMedicines() {
        return prescriptionRepo.findTopMedicines();
    }

    public String updatePrescription(UUID id, Prescription updatedPrescription) {
        Optional<Prescription> existing = prescriptionRepo.findById(id);
        if (existing.isEmpty()) {
            return "The prescription with that id does not exist";
        }
        Prescription prescription = existing.get();
        prescription.setMedicineName(updatedPrescription.getMedicineName());
        prescription.setDosage(updatedPrescription.getDosage());
        prescription.setDurationDays(updatedPrescription.getDurationDays());
        prescriptionRepo.save(prescription);
        return "Prescription updated successfully";
    }

    public String deletePrescription(UUID id) {
        if (!prescriptionRepo.existsById(id)) {
            return "The prescription with that id does not exist";
        }
        prescriptionRepo.deleteById(id);
        return "Prescription deleted successfully";
    }
}
