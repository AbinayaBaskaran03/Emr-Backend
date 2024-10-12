package com.emrheathgroup.backend.Entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import com.emrheathgroup.backend.Enumeration.Gender;
import com.emrheathgroup.backend.Enumeration.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Patient")
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Patient_Id")
	private Integer patientId;
	
	@Lob
	@Column(name="profile",columnDefinition = "LONGBLOB")
	private byte[] profile;

	@Column(name = "national_id")
	private String nationalId;

	@Column(name = "mrd_no")
	private String mrdNo;

	@Column(name = "Name")
	private String name;

	@Column(name = "dob")
	private Date dob;

	@Column(name = "age")
	private Integer age;

	@Column(name = "Gender")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "nationality")
	private String nationality;

	@Column(name = "marital_status")
	private String maritalStatus;

	@Column(name = "visa_type")
	private String visaType;

	@Column(name = "other_id")
	private String otherId;

	@Column(name = "occupation")
	private String occupation;

	@Column(name = "address")
	private String address;

	@Column(name = "phone_no")
	private String phoneNo;

	@Column(name = "insurance_name")
	private String insuranceName;
	
	@Column(name = "problem")
	private String problem;
	
	@Column(name = "symptoms")
	private String symptoms;

	@CreationTimestamp
	private Date createdOn;

	@CreatedBy
	@Column(name = "Created_by")
	private String createdBy;

	@UpdateTimestamp
	private Date updateOn;

	@CreatedBy
	@Column(name = "Updated_by")
	private String updatedBy;

	@Column(name = "Is_Active")
	@Enumerated(EnumType.STRING)
	private Status status;

}
