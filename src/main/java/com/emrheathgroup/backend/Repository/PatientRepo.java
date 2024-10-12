package com.emrheathgroup.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.Patient;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Integer>, JpaSpecificationExecutor<Patient> {

	Optional<Patient> findByMrdNo(String mrdNo);

	Optional<Patient> findTopByOrderByMrdNoDesc();

	boolean existsByEmailId(String email);

	boolean existsByPhoneNo(String mobile);

	Optional<Patient> findByPhoneNo(String phoneNo);

	Optional<Patient> findByEmailId(String email);

	List<Patient> findByMrdNoOrPhoneNoOrEmailId(String mrdNo, String phoneNo, String email);

	List<Patient> findAllByName(String name);

	List<Patient> findAllByMrdNo(String mrdNo);

	List<Patient> findAllByPhoneNo(String phoneNo);

	List<Patient> findAllByEmailId(String email);

	Optional<Patient> findByName(String name);

	boolean existsByMrdNo(String mrdNo);

	Patient findByOtherId(String otherId);

	Optional<Patient> findByNationalId(String nationalId);

	@Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
            "LOWER(p.emailId) LIKE LOWER(CONCAT('%', :searchKey, '%')) OR " +
            "p.phoneNo LIKE CONCAT('%', :searchKey, '%') OR " +
            "p.mrdNo LIKE CONCAT('%', :searchKey, '%') OR " +
            "p.nationalId LIKE CONCAT('%', :searchKey, '%') OR " +
            "p.otherId LIKE CONCAT('%', :searchKey, '%')")
    Page<Patient> findByAnyField(@Param("searchKey") String searchKey, Pageable pageable);
}
