package com.emrheathgroup.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.PatientDependent;

@Repository
public interface PatientDependentRepo extends JpaRepository<PatientDependent, Integer>{

}
