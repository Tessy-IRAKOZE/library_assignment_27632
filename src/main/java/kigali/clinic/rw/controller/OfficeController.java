package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;


@RestController
@RequestMapping(value ="/api/office")
@CrossOrigin ("*")
public class OfficeController {

    @Autowired
    private OfficeService offServe;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveOffice(@RequestBody Office office) {
        String returnedMessage = offServe.saveOffice(office);
        if (returnedMessage.equals("Office is saved successfully")) {
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/free")
    public ResponseEntity<List<Office>> findFreeOffices() {
        return new ResponseEntity<>(offServe.findFreeOffices(), HttpStatus.OK);
    }

    @GetMapping(value = "/busiest")
    public ResponseEntity<?> findBusiestOffice() {
        List<Object[]> result = offServe.findBusiestOffice();
        if (result.isEmpty()) {
            return new ResponseEntity<>("No appointments yet", HttpStatus.OK);
        }
        return new ResponseEntity<>(result.get(0), HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Office>> findAll() {
        return new ResponseEntity<>(offServe.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<Office> office = offServe.findById(id);
        if (office.isPresent()) {
            return new ResponseEntity<>(office.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Office not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateOffice(@PathVariable UUID id, @RequestBody Office office) {
        String message = offServe.updateOffice(id, office);
        if (message.equals("Office updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id) {
        String message = offServe.deleteOffice(id);
        if (message.equals("Office deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

        @DeleteMapping(value = "/deleteByNumber/{officeNumber}")
        public ResponseEntity<?> deleteOfficeByOfficeNumber(@PathVariable int officeNumber) {
        String message = offServe.deleteOfficeByOfficeNumber(officeNumber);
        if (message.equals("deleted")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
    }

}


