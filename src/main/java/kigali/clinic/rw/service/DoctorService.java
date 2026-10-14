package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private OfficeRepository officeRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public List<Doctor> findBySpecializationName(String name) {
        return doctorRepo.findBySpecializationName(name);
    }

    public List<Doctor> findDoctorsWithoutOffice() {
        return doctorRepo.findDoctorsWithoutOffice();
    }

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

    @Transactional
    public String addSpecialization(UUID doctorId, UUID specializationId) {
        Optional<Doctor> foundDoctor = doctorRepo.findById(doctorId);
        if (foundDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Specialization> foundSpecialization = specializationRepo.findById(specializationId);
        if (foundSpecialization.isEmpty()) {
            return "That specialization does not exist";
        }
        Doctor doctor = foundDoctor.get();
        Specialization specialization = foundSpecialization.get();
        if (doctor.getSpecializations().contains(specialization)) {
            return "The doctor already has that specialization";
        }
        doctor.getSpecializations().add(specialization);
        doctorRepo.save(doctor);
        return "Specialization added to the doctor";
    }

    @Transactional
    public String removeSpecialization(UUID doctorId, UUID specializationId) {
        Optional<Doctor> foundDoctor = doctorRepo.findById(doctorId);
        if (foundDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Specialization> foundSpecialization = specializationRepo.findById(specializationId);
        if (foundSpecialization.isEmpty()) {
            return "That specialization does not exist";
        }
        Doctor doctor = foundDoctor.get();
        Specialization specialization = foundSpecialization.get();
        if (!doctor.getSpecializations().contains(specialization)) {
            return "The doctor does not have that specialization";
        }
        doctor.getSpecializations().remove(specialization);
        doctorRepo.save(doctor);
        return "Specialization removed from the doctor";
    }

    public List<Appointment> findAgenda(UUID doctorId, LocalDate date) {
        if (!doctorRepo.existsById(doctorId)) {
            return null;
        }
        return appointmentRepo.findByDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, AppointmentStatus.CANCELLED);
    }

    public List<Doctor> searchByName(String name) {
        return doctorRepo.searchByName(name);
    }

    public String changeOffice(UUID doctorId, int officeNumber) {
        Optional<Doctor> foundDoctor = doctorRepo.findById(doctorId);
        if (foundDoctor.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Office> foundOffice = officeRepo.findByOfficeNumber(officeNumber);
        if (foundOffice.isEmpty()) {
            return "The office with that office number does not exist";
        }
        Office office = foundOffice.get();
        if (doctorRepo.existsByOfficeIdAndIdNot(office.getId(), doctorId)) {
            return "That office is taken by other doctor";
        }
        Doctor doctor = foundDoctor.get();
        doctor.setOffice(office);
        doctorRepo.save(doctor);
        return "Office changed successfully";
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
