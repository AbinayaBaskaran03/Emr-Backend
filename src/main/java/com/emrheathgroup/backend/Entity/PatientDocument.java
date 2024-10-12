package com.emrheathgroup.backend.Entity;

import java.util.Date;

import com.emrheathgroup.backend.Enumeration.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pt_document")
public class PatientDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_id")
	private Integer documentId;

	@ManyToOne
	@JoinColumn(name = "Patient_Id")
	private Patient patient;

	@Column(name = "doc_national_id")
	private String docNationalId;

	@Column(name = "doc_type")
	private String docType;

	@Column(name = "if_other_type")
	private String ifOtherType;

	@Lob
	@Column(name = "document",columnDefinition = "LONGBLOB")
	private byte[] document;

}
