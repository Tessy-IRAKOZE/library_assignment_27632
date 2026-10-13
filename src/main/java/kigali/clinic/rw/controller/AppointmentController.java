package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping(value = {"/api/appointment", "/api/appointments"})
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping(value = "/by-status")
    public ResponseEntity<List<Appointment>> findByStatus(@RequestParam AppointmentStatus status) {
        return new ResponseEntity<>(appointmentService.findByStatus(status), HttpStatus.OK);
    }

    @GetMapping(value = "/between")
    public ResponseEntity<List<Appointment>> findBetweenDates(@RequestParam String start, @RequestParam String end) {
        return new ResponseEntity<>(appointmentService.findBetweenDates(LocalDate.parse(start), LocalDate.parse(end)), HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment) {
        String message = appointmentService.saveAppointment(appointment);
        if (message.equals("Appointment date cannot be in the past")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else if (message.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Appointment>> findAll() {
        return new ResponseEntity<>(appointmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(value = "/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> findByDoctorId(@PathVariable UUID doctorId) {
        return new ResponseEntity<>(appointmentService.findByDoctorId(doctorId), HttpStatus.OK);
    }
    @GetMapping(value = "/patient/{patientId}")
    public ResponseEntity<List<Appointment>> findByPatientId(@PathVariable UUID patientId) {
        return new ResponseEntity<>(appointmentService.findByPatientId(patientId), HttpStatus.OK);
    }
    @GetMapping(value = "/doctor/{doctorId}/pending")
    public ResponseEntity<List<Appointment>> findPendingByDoctor(@PathVariable UUID doctorId) {
        return new ResponseEntity<>(appointmentService.findPendingByDoctorOrderByDate(doctorId), HttpStatus.OK);
    }
    @GetMapping(value = "/pending/past")
    public ResponseEntity<List<Appointment>> findPendingPastAppointments() {
        return new ResponseEntity<>(appointmentService.findPendingAppointmentsBeforeDate(), HttpStatus.OK);
    }
    @GetMapping(value = "/count-per-doctor")
    public ResponseEntity<List<Object[]>> countAppointmentsPerDoctor() {
        return new ResponseEntity<>(appointmentService.countAppointmentsPerDoctor(), HttpStatus.OK);
    }
    @GetMapping(value = "/stats/by-status")
    public ResponseEntity<List<Object[]>> countAppointmentsByStatus() {
        return new ResponseEntity<>(appointmentService.countAppointmentsByStatus(), HttpStatus.OK);
    }
    @PatchMapping(value = "/cancel-day")
    public ResponseEntity<?> cancelAppointmentsForDoctorOnDate(@RequestParam UUID doctorId, @RequestParam String date) {
        LocalDate appointmentDate = LocalDate.parse(date);
        String message = appointmentService.cancelAppointmentsForDoctorOnDate(doctorId, appointmentDate);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(value = "/specialization/{specializationId}")
    public ResponseEntity<List<Appointment>> findBySpecialization(@PathVariable UUID specializationId) {
        return new ResponseEntity<>(appointmentService.findAppointmentsBySpecialization(specializationId), HttpStatus.OK);
    }

    @GetMapping(value = "/doctor/{doctorId}/appointments")
    public ResponseEntity<Page<Appointment>> findDoctorAppointmentsPaginated(
            @PathVariable UUID doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(appointmentService.findDoctorAppointmentsPaginated(doctorId, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<Appointment>> findAppointmentsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "appointmentDate,desc") String sort) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String direction = sortParts.length > 1 ? sortParts[1] : "asc";
        return new ResponseEntity<>(appointmentService.findAppointmentsPage(page, size, sortBy, direction), HttpStatus.OK);
    }

    @DeleteMapping(value = "/cancelled-before")
    public ResponseEntity<?> deleteCancelledBefore(@RequestParam String date) {
        String message = appointmentService.deleteCancelledBefore(LocalDate.parse(date));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestParam AppointmentStatus status) {
        String message = appointmentService.updateStatus(id, status);
        if (message.equals("Appointment status updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/{id}/reschedule")
    public ResponseEntity<?> rescheduleAppointment(@PathVariable UUID id, @RequestParam String newDate) {
        String message = appointmentService.rescheduleAppointment(id, LocalDate.parse(newDate));
        if (message.equals("Appointment rescheduled successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment) {
        String message = appointmentService.updateAppointment(id, appointment);
        if (message.equals("Appointment updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id) {
        String message = appointmentService.deleteAppointment(id);
        if (message.equals("Appointment deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
