package com.emrheathgroup.backend.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.BranchMaster;
import com.emrheathgroup.backend.Entity.Doctor;
import com.emrheathgroup.backend.Entity.DoctorRoster;

@Repository
public interface DoctorRosterRepo extends JpaRepository<DoctorRoster, Integer> {

	List<DoctorRoster> findByBranchMasterAndDateAndDoctor(BranchMaster branch, Date appointmentDate, Doctor doctor);

	List<DoctorRoster> findByDoctor(Doctor doctor);

	List<DoctorRoster> findByDate(Date date);

	@Query("SELECT r FROM DoctorRoster r WHERE r.doctor = :doctor AND FUNCTION('DATE', r.date) = FUNCTION('DATE', :appointmentDate)")
    Optional<DoctorRoster> findByDoctorAndDate(@Param("doctor") Doctor doctor,
            @Param("appointmentDate") Date appointmentDate);

    @Query(value = "SELECT dr.* FROM doctor_roster dr JOIN doctor d ON d.Doctor_Id = dr.doctor_id WHERE dr.date = :date AND dr.branch_id = :branchId AND d.speciality_id = :specialityId", nativeQuery = true)
    List<DoctorRoster> findByDateBranchAndSpecialityNative(@Param("date") Date date,
            @Param("branchId") Integer branchId, @Param("specialityId") Integer specialityId);

    @Query("SELECT dr FROM DoctorRoster dr " +
    	       "JOIN dr.doctor d " +
    	       "JOIN DoctorSpecialityMapping dsm ON d.doctorId = dsm.doctor.doctorId " +
    	       "WHERE dr.date = :date " +
    	       "AND dr.branchMaster.branchId = :branchId " + 
    	       "AND dsm.speciality.specialityId = :specialityId " + 
    	       "AND dr.status = true")
    	List<DoctorRoster> findAvailableDoctors(@Param("date") Date date, 
    	                                        @Param("branchId") Integer branchId, 
    	                                        @Param("specialityId") Integer specialityId);



}