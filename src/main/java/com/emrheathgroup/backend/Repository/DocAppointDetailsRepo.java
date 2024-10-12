package com.emrheathgroup.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.DocAppointmentDetails;

@Repository
public interface DocAppointDetailsRepo extends JpaRepository<DocAppointmentDetails, Integer> {

}
