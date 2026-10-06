package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping(value = "/api/doctor")
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
