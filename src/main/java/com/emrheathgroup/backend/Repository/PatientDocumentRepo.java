package com.emrheathgroup.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emrheathgroup.backend.Entity.PatientDocument;

@Repository
public interface PatientDocumentRepo extends JpaRepository<PatientDocument, Integer> {

}
