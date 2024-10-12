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
@Table(name = "Doctor")
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Doctor_Id")
	private Integer doctorId;

	@ManyToOne
	@JoinColumn(name = "branch_Id")
	private BranchMaster branchMaster;

	@Column(name = "Is_Active")
	private Boolean active;

	@Column(name = "Name")
	private String name;

	@Column(name = "Experience")
	private String experience;

	@Column(name = "Contact_Info")
	private String contactInfo;

}
