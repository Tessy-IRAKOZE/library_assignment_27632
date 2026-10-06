package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    public String saveSpecialization(Specialization specialization) {
        specializationRepo.save(specialization);
        return "Specialization saved successfully";
    }

    public List<Specialization> findAll() {
        return specializationRepo.findAll();
    }

    public Optional<Specialization> findById(UUID id) {
        return specializationRepo.findById(id);
    }

    public String updateSpecialization(UUID id, Specialization updatedSpecialization) {
        Optional<Specialization> existing = specializationRepo.findById(id);
        if (existing.isEmpty()) {
            return "Specialization not found";
        }
        Specialization specialization = existing.get();
        specialization.setName(updatedSpecialization.getName());
        specializationRepo.save(specialization);
        return "Specialization updated successfully";
    }

    @Transactional
    public String assignDoctorToSpecialization(UUID specializationId, UUID doctorId) {
        System.out.println("Looking for specialization: " + specializationId);
        Optional<Specialization> specialization = specializationRepo.findById(specializationId);
        System.out.println("Specialization found: " + specialization.isPresent());
        Optional<Doctor> doctor = doctorRepo.findById(doctorId);
        System.out.println("Doctor found: " + doctor.isPresent());
        if (specialization.isEmpty()) {
            return "Specialization not found";
        }
        if (doctor.isEmpty()) {
            return "Doctor not found";
        }
        System.out.println("Getting doctors list...");
        List<Doctor> doctors = specialization.get().getDoctors();
        System.out.println("Doctors list size: " + doctors.size());
        if (doctors.contains(doctor.get())) {
            return "Doctor already assigned to this specialization";
        }
        doctors.add(doctor.get());
        specializationRepo.save(specialization.get());
        return "Doctor assigned to specialization successfully";
    }

    public String deleteSpecialization(UUID id) {
        if (!specializationRepo.existsById(id)) {
            return "Specialization not found";
        }
        specializationRepo.deleteById(id);
        return "Specialization deleted successfully";
    }
}
