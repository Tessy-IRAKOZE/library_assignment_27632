package kigali.clinic.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByDoctorId(UUID doctorId);
    List<Appointment> findByPatientId(UUID patientId);
    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateAsc(UUID doctorId, AppointmentStatus status);
    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);
    boolean existsByDoctorIdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate date, AppointmentStatus status);
    boolean existsByDoctorIdAndAppointmentDateAndStatusNotAndIdNot(UUID doctorId, LocalDate date, AppointmentStatus status, UUID id);

    @Query("SELECT a FROM Appointment a WHERE a.status = 'PENDING' AND a.appointmentDate < :today")
    List<Appointment> findPendingAppointmentsBeforeDate(LocalDate today);

    @Query("SELECT a.doctor.firstName, a.doctor.lastName, COUNT(a) FROM Appointment a GROUP BY a.doctor ORDER BY COUNT(a) DESC")
    List<Object[]> countAppointmentsPerDoctor();

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :cancelled WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status <> :completed")
    int cancelAppointmentsForDoctorOnDate(UUID doctorId, LocalDate date, AppointmentStatus cancelled, AppointmentStatus completed);

    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.status = :status AND a.appointmentDate < :date")
    int deleteCancelledBefore(AppointmentStatus status, LocalDate date);

    @Query("SELECT a FROM Appointment a JOIN a.doctor d JOIN d.specializations s WHERE s.id = :specializationId")
    List<Appointment> findAppointmentsBySpecialization(UUID specializationId);

    Page<Appointment> findByDoctorIdOrderByAppointmentDateAsc(UUID doctorId, Pageable pageable);
    Page<Appointment> findAllByOrderByAppointmentDateAsc(Pageable pageable);
    Page<Appointment> findAllByOrderByAppointmentDateDesc(Pageable pageable);
}
