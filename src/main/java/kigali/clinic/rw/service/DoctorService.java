package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    public String saveDoctor(Doctor doctor) {
        doctorRepo.save(doctor);
        return "Doctor saved successfully";
    }

    public List<Doctor> findAll() {
        return doctorRepo.findAll();
    }

    public Optional<Doctor> findById(UUID id) {
        return doctorRepo.findById(id);
    }

    public String updateDoctor(UUID id, Doctor updatedDoctor) {
        Optional<Doctor> existing = doctorRepo.findById(id);
        if (existing.isEmpty()) {
            return "Doctor not found";
        }
        Doctor doctor = existing.get();
        doctor.setFirstName(updatedDoctor.getFirstName());
        doctor.setLastName(updatedDoctor.getLastName());
        doctor.setDateOfBirth(updatedDoctor.getDateOfBirth());
        doctor.setOffice(updatedDoctor.getOffice());
        doctorRepo.save(doctor);
        return "Doctor updated successfully";
    }

    public String deleteDoctor(UUID id) {
        Optional<Doctor> existing = doctorRepo.findById(id);
        if (existing.isEmpty()) {
            return "Doctor not found";
        }
        Doctor doctor = existing.get();
        doctor.setOffice(null);
        doctorRepo.save(doctor);
        doctorRepo.deleteById(id);
        return "Doctor deleted successfully";
    }
}
