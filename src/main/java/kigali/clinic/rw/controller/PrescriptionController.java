package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;

@RestController
@RequestMapping(value = "/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> savePrescription(@RequestParam UUID appointmentId, @RequestBody Prescription prescription) {
        String message = prescriptionService.savePrescription(appointmentId, prescription);
        if (message.equals("Prescription saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else if (message.equals("Prescriptions are only for COMPLETED appointments")) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/appointment/{appointmentId}")
    public ResponseEntity<?> findByAppointment(@PathVariable UUID appointmentId) {
        List<Prescription> prescriptions = prescriptionService.findByAppointment(appointmentId);
        if (prescriptions == null) {
            return new ResponseEntity<>("The appointment with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable UUID id, @RequestBody Prescription prescription) {
        String message = prescriptionService.updatePrescription(id, prescription);
        if (message.equals("Prescription updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable UUID id) {
        String message = prescriptionService.deletePrescription(id);
        if (message.equals("Prescription deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/stats/top-medicines")
    public ResponseEntity<List<Object[]>> findTopMedicines() {
        return new ResponseEntity<>(prescriptionService.findTopMedicines(), HttpStatus.OK);
    }

    @GetMapping(value = "/patient/{ssNumber}")
    public ResponseEntity<List<Prescription>> findByPatientSsNumber(@PathVariable String ssNumber) {
        return new ResponseEntity<>(prescriptionService.findByPatientSsNumber(ssNumber), HttpStatus.OK);
    }
}
