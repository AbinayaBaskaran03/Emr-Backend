package com.emrheathgroup.backend.Repository.Doctor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

	@Query(value = "SELECT count(*) FROM DoctorAppointment da join Doctor d on da.doctor.doctorId = d.doctorId "
			+ "WHERE date(da.appointmentDate) = date(:appointmentDate) and da.doctor.doctorId = :doctorId")
	Long getTodayPatientCount(int doctorId, Date appointmentDate);

	@Query(value = "SELECT sum(da.revenue) FROM DoctorAppointment da join Doctor d on da.doctor.doctorId = d.doctorId "
			+ "WHERE date(da.appointmentDate) = date(:appointmentDate) and da.doctor.doctorId = :doctorId")
	Long getTodayTotalRevenue(int doctorId, Date appointmentDate);

	@Query(value = "SELECT count(*) FROM DoctorAppointment da join Doctor d on da.doctor.doctorId = d.doctorId "
			+ "WHERE date(da.appointmentDate) = date(:appointmentDate) and da.doctor.doctorId = :doctorId and da.scheduleType = :scheduleType")
	Long getPatientApptmntCount(int doctorId, Date appointmentDate, String scheduleType);

	@Query(value = "SELECT da.appointmentDate as lastConsultationDate"
			+ "	FROM DoctorAppointment da JOIN DoctorSpecialityMapping dsm on dsm.doctor.doctorId = da.doctor.doctorId  and da.speciality.specialityId = dsm.speciality.specialityId "
			+ " WHERE date(da.appointmentDate) < date(:appointmentDate) AND da.doctor.doctorId = :doctorId AND da.patient.patientId = :patientId AND da.speciality.specialityId = :specId "
			+ " and (:scheduleType is null or da.scheduleType like CONCAT('', :scheduleType, '%'))")
	List<Date> getLastConsultaionDate(int doctorId, int patientId, int specId, Date appointmentDate,
			String scheduleType, Pageable pageable);

	@Query(value = "SELECT ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) as sNo, p.name as name, p.mrdNo as mrdNo, da.insuranceName as insuranceName"
			+ " ,da.doctor.doctorId as doctorId, da.patient.patientId as patientId, da.speciality.specialityId as specialityId"
			+ " ,da.scheduleType as scheduleType, UPPER(SUBSTRING(da.appointmentStatus, 1, 1)) as status"
			+ " FROM DoctorAppointment da join Doctor d on da.doctor.doctorId = d.doctorId"
			+ " join Patient p on p.patientId = da.patient.patientId WHERE date(da.appointmentDate) = date(:appointmentDate) and da.doctor.doctorId = :doctorId"
			+ " and (:ptName is null or p.name like CONCAT('%', :ptName, '%')) and (:status is null or da.appointmentStatus like CONCAT('', :status, '%'))")
	List<Map<String, Object>> getTodayPatientData(int doctorId, Date appointmentDate, String ptName, String status,
			Pageable pageable);

	@Query(value = "SELECT ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) as sNo, p.name as name, p.mrdNo as mrdNo, da.insuranceName as insuranceName"
			+ " ,da.doctor.doctorId as doctorId, da.patient.patientId as patientId, da.speciality.specialityId as specialityId, da.scheduleType"
			+ " ,da.scheduleType as scheduleType, UPPER(SUBSTRING(da.appointmentStatus, 1, 1)) as status"
			+ " FROM DoctorAppointment da join Doctor d on da.doctor.doctorId = d.doctorId"
			+ " join Patient p on p.patientId = da.patient.patientId WHERE date(da.appointmentDate) = date(:appointmentDate) and da.doctor.doctorId = :doctorId "
			+ " and da.scheduleType = :scheduleType and (:ptName is null or p.name like CONCAT('%', :ptName, '%')) and (:status is null or da.appointmentStatus like CONCAT('', :status, '%'))")
	List<Map<String, Object>> getPatientApptmntData(int doctorId, Date appointmentDate, String scheduleType,
			String ptName, String status, Pageable pageable);

	@Query(value = "SELECT ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) as sNo, p.name as name, p.mrdNo as mrdNo, da.insuranceName as insuranceName, da.appointmentDate as date, da.revenue as revenue "
			+ " FROM DoctorAppointment da join Doctor d on da.doctor.doctorId = d.doctorId"
			+ " join Patient p on p.patientId = da.patient.patientId"
			+ " WHERE date(da.appointmentDate) = date(:appointmentDate) and da.doctor.doctorId = :doctorId")
	List<Map<String, Object>> getTodayTotalRevenueData(int doctorId, Date appointmentDate, Pageable pageable);

}
