package com.emrheathgroup.backend.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.BranchMaster;
import com.emrheathgroup.backend.Entity.Doctor;
import com.emrheathgroup.backend.Entity.DoctorAppointment;

@Repository
public interface DoctorAppointmentRepo
		extends JpaRepository<DoctorAppointment, Integer>, JpaSpecificationExecutor<DoctorAppointment> {

	List<DoctorAppointment> findByDoctor_DoctorIdAndBranchMasterAndAppointmentDateAndAppointmentTime(Long doctorId,
			BranchMaster branch, Date appointmentDate, Time appointmentTime);

	Optional<DoctorAppointment> findByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(Long doctorId,
			Date appointmentDate, Time appointmentTime);

	boolean existsByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(Doctor doctor, Date newAppointmentDate,
			Time newAppointmentTime);

	List<DoctorAppointment> findByDoctor_DoctorIdAndAppointmentDate(Integer doctorId, Date appointmentDate);

	List<DoctorAppointment> findByDoctor_DoctorIdAndSpeciality_SpecialityIdAndAppointmentDate(Integer doctorId,
			Integer specialityId, Date appointmentDate);

	List<DoctorAppointment> findByDoctor_DoctorId(Integer doctorId);

}
