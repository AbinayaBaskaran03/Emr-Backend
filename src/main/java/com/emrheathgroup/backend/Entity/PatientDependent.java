package com.emrheathgroup.backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pt_dependent")
public class PatientDependent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dependent_id")
	private Integer dependentId;
	
	@ManyToOne
	@JoinColumn(name = "Patient_Id")
	private Patient patient;
	
	@Column(name = "dep_national_id")
	private String depNationalId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "age")
	private Integer age;
	
	@Column(name = "relation")
	private String relation;
}
