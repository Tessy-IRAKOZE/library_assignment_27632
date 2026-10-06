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

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> findBetweenDates(LocalDate start, LocalDate end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public String saveAppointment(Appointment appointment) {
        boolean isBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateAndStatusNot(
            appointment.getDoctor().getId(),
            appointment.getAppointmentDate(),
            AppointmentStatus.CANCELLED
        );
        if (isBooked) {
            return "Doctor is already booked on that date";
        }
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

    public String updateAppointment(UUID id, Appointment updatedAppointment) {
        Optional<Appointment> existing = appointmentRepo.findById(id);
        if (existing.isEmpty()) {
            return "Appointment not found";
        }
        Appointment appointment = existing.get();
        appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        appointment.setReason(updatedAppointment.getReason());
        appointment.setStatus(updatedAppointment.getStatus());
        appointment.setDoctor(updatedAppointment.getDoctor());
        appointment.setPatient(updatedAppointment.getPatient());
        appointmentRepo.save(appointment);
        return "Appointment updated successfully";
    }

    public String deleteAppointment(UUID id) {
        if (!appointmentRepo.existsById(id)) {
            return "Appointment not found";
        }
        appointmentRepo.deleteById(id);
        return "Appointment deleted successfully";
    }
}
