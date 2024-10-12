package com.emrheathgroup.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.Speciality;

@Repository
public interface SpecialityRepo extends JpaRepository<Speciality, Integer> {

}
