package com.emrheathgroup.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.DoctorSpecialityMapping;
import com.emrheathgroup.backend.Entity.Speciality;

@Repository
public interface DocSpecialityMapRepo extends JpaRepository<DoctorSpecialityMapping, Integer> {

	List<DoctorSpecialityMapping> findBySpecialityAndDoctor_BranchMaster_BranchId(Speciality speciality, Integer branchId);

	List<DoctorSpecialityMapping> findBySpecialitySpecialityId(Integer specialityId);

	List<DoctorSpecialityMapping> findByDoctor_DoctorId(Integer id);

	


}
