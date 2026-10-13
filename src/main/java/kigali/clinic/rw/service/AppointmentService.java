package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> findBetweenDates(LocalDate start, LocalDate end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }
    public String saveAppointment(Appointment appointment) {
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }
        boolean isBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
            appointment.getDoctor().getId(),
            appointment.getAppointmentDate(),
            AppointmentStatus.CANCELLED
        );
        if (isBooked) {
            return "Doctor is already booked on that date";
        }
        appointment.setStatus(AppointmentStatus.PENDING);
        appointmentRepo.save(appointment);
        return "Appointment saved successfully";
    }
    public List<Appointment> findAll() {
        return appointmentRepo.findAll();
    }
    public Optional<Appointment> findById(UUID id) {
        return appointmentRepo.findById(id);
    }
    public List<Appointment> findByDoctorId(UUID doctorId) {
        return appointmentRepo.findByDoctorId(doctorId);
    }
    public List<Appointment> findByPatientId(UUID patientId) {
        return appointmentRepo.findByPatientId(patientId);
    }
    public List<Appointment> findPendingByDoctorOrderByDate(UUID doctorId) {
        return appointmentRepo.findByDoctorIdAndStatusOrderByAppointmentDateAsc(doctorId, AppointmentStatus.PENDING);
    }
    public List<Appointment> findPendingAppointmentsBeforeDate() {
        return appointmentRepo.findPendingAppointmentsBeforeDate(LocalDate.now());
    }
    public List<Object[]> countAppointmentsPerDoctor() {
        return appointmentRepo.countAppointmentsPerDoctor();
    }
    public List<Object[]> countAppointmentsByStatus() {
        return appointmentRepo.countAppointmentsByStatus();
    }
    public String cancelAppointmentsForDoctorOnDate(UUID doctorId, LocalDate date) {
        int count = appointmentRepo.cancelAppointmentsForDoctorOnDate(
            doctorId,
            date,
            AppointmentStatus.CANCELLED,
            AppointmentStatus.COMPLETED
        );
        return count + " appointments cancelled";
    }
    public List<Appointment> findAppointmentsBySpecialization(UUID specializationId) {
        return appointmentRepo.findAppointmentsBySpecialization(specializationId);
    }
    public Page<Appointment> findDoctorAppointmentsPaginated(UUID doctorId, int page, int size) {
        return appointmentRepo.findByDoctorIdOrderByAppointmentDateAsc(doctorId, PageRequest.of(page, size, Sort.by("appointmentDate").ascending()));
    }
    public Page<Appointment> findAppointmentsPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return appointmentRepo.findAll(PageRequest.of(page, size, sort));
    }
    public String deleteCancelledBefore(LocalDate date) {
        int count = appointmentRepo.deleteCancelledBefore(AppointmentStatus.CANCELLED, date);
        return count + " appointments deleted";
    }
    public String updateStatus(UUID id, AppointmentStatus newStatus) {
        Optional<Appointment> existing = appointmentRepo.findById(id);
        if (existing.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = existing.get();
        AppointmentStatus current = appointment.getStatus();
        boolean allowed = false;
        if (current == AppointmentStatus.PENDING) {
            if (newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED) {
                allowed = true;
            }
        } else if (current == AppointmentStatus.CONFIRMED) {
            if (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED) {
                allowed = true;
            }
        }
        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }
        appointment.setStatus(newStatus);
        appointmentRepo.save(appointment);
        return "Appointment status updated successfully";
    }

    public String rescheduleAppointment(UUID id, LocalDate newDate) {
        Optional<Appointment> existing = appointmentRepo.findById(id);
        if (existing.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = existing.get();
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return "A COMPLETED appointment cannot be rescheduled";
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return "A CANCELLED appointment cannot be rescheduled";
        }
        if (newDate.isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }
        boolean isBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNotAndIdNot(
            appointment.getDoctor().getId(),
            newDate,
            AppointmentStatus.CANCELLED,
            id
        );
        if (isBooked) {
            return "Doctor is already booked on that date";
        }
        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointmentRepo.save(appointment);
        return "Appointment rescheduled successfully";
    }

    public String updateAppointment(UUID id, Appointment updatedAppointment) {
        Optional<Appointment> existing = appointmentRepo.findById(id);
        if (existing.isEmpty()) {
            return "Appointment not found";
        }
        Appointment appointment = existing.get();
        appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        appointment.setReason(updatedAppointment.getReason());
        appointment.setDoctor(updatedAppointment.getDoctor());
        appointment.setPatient(updatedAppointment.getPatient());
        appointmentRepo.save(appointment);
        return "Appointment updated successfully";
    }
    public String deleteAppointment(UUID id) {
        if (!appointmentRepo.existsById(id)) {
            return "Appointment not found";
        }
        if (prescriptionRepo.existsByAppointmentId(id)) {
            return "That appointment has prescriptions, delete them first";
        }
        appointmentRepo.deleteById(id);
        return "Appointment deleted successfully";
    }
}
