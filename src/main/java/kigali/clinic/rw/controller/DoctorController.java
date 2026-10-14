package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping(value = {"/api/doctor", "/api/doctors"})
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping(value = "/without-office")
    public ResponseEntity<List<Doctor>> findDoctorsWithoutOffice() {
        return new ResponseEntity<>(doctorService.findDoctorsWithoutOffice(), HttpStatus.OK);
    }

    @GetMapping(value = "/by-specialization")
    public ResponseEntity<List<Doctor>> findBySpecializationName(@RequestParam String name) {
        return new ResponseEntity<>(doctorService.findBySpecializationName(name), HttpStatus.OK);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor) {
        String message = doctorService.saveDoctor(doctor);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Doctor>> findAll() {
        return new ResponseEntity<>(doctorService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<Doctor> doctor = doctorService.findById(id);
        if (doctor.isPresent()) {
            return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> addSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
        String message = doctorService.addSpecialization(doctorId, specializationId);
        if (message.equals("Specialization added to the doctor")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("The doctor already has that specialization")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> removeSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
        String message = doctorService.removeSpecialization(doctorId, specializationId);
        if (message.equals("Specialization removed from the doctor")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("The doctor does not have that specialization")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{doctorId}/agenda")
    public ResponseEntity<?> findAgenda(@PathVariable UUID doctorId, @RequestParam String date) {
        List<Appointment> appointments = doctorService.findAgenda(doctorId, LocalDate.parse(date));
        if (appointments == null) {
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Doctor>> searchByName(@RequestParam String name) {
        return new ResponseEntity<>(doctorService.searchByName(name), HttpStatus.OK);
    }

    @PatchMapping(value = "/{doctorId}/office")
    public ResponseEntity<?> changeOffice(@PathVariable UUID doctorId, @RequestParam int officeNumber) {
        String message = doctorService.changeOffice(doctorId, officeNumber);
        if (message.equals("Office changed successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else if (message.equals("That office is taken by other doctor")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {
        String message = doctorService.updateDoctor(id, doctor);
        if (message.equals("Doctor updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {
        String message = doctorService.deleteDoctor(id);
        if (message.equals("Doctor deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
