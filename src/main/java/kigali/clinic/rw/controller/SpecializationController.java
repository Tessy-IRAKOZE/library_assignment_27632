package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping(value = "/api/specialization")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveSpecialization(@RequestBody Specialization specialization) {
        String message = specializationService.saveSpecialization(specialization);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Specialization>> findAll() {
        return new ResponseEntity<>(specializationService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<Specialization> specialization = specializationService.findById(id);
        if (specialization.isPresent()) {
            return new ResponseEntity<>(specialization.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Specialization not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{specializationId}/assign-doctor/{doctorId}")
    public ResponseEntity<?> assignDoctor(@PathVariable UUID specializationId, @PathVariable UUID doctorId) {
        String message = specializationService.assignDoctorToSpecialization(specializationId, doctorId);
        if (message.equals("Doctor assigned to specialization successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateSpecialization(@PathVariable UUID id, @RequestBody Specialization specialization) {
        String message = specializationService.updateSpecialization(id, specialization);
        if (message.equals("Specialization updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteSpecialization(@PathVariable UUID id) {
        String message = specializationService.deleteSpecialization(id);
        if (message.equals("Specialization deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }
}
