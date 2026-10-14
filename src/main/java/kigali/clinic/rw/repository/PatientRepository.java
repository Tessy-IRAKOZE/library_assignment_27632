package kigali.clinic.rw.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import kigali.clinic.rw.domain.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findByFirstName(String firstName);
    List<Patient> findByLastName(String lastName);
    List<Patient> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);
    boolean existsBySsNumber(String ssNumber);

    @Query("SELECT p FROM Patient p JOIN p.appointments a GROUP BY p HAVING COUNT(a) >= :min ORDER BY COUNT(a) DESC")
    List<Patient> findFrequentPatients(int min);

    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<Patient> findPatientsByDoctorId(UUID doctorId);
}
