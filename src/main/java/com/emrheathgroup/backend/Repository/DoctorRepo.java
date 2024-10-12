package com.emrheathgroup.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.BranchMaster;
import com.emrheathgroup.backend.Entity.Doctor;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {

	List<Doctor> findByBranchMaster(BranchMaster branchMaster);

	//List<Doctor> findBySpecialityId(Integer specialistId);

	//S@Query("SELECT d FROM Doctor d JOIN DoctorRoster dr ON d = dr.doctorId WHERE d.specialityId = :specialityId AND dr.branchId.branchId = :branchId AND dr.date = :appointmentDate")
	//List<Doctor> findBySpecialityIdAndBranchIdAndDate(Integer specialityId, Integer branchId, Date appointmentDate);

	//List<Doctor> findBySpecialityIdAndBranchMasterBranchId(Integer specialityId, Integer branchId);

}
