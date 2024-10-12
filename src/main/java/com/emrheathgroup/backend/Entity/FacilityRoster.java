package com.emrheathgroup.backend.Entity;

import java.sql.Time;
import java.util.Date;

import com.emrheathgroup.backend.Enumeration.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "facility_roster")
public class FacilityRoster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roster_id")
	private Integer rosterId;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private BranchMaster branchId;

	@Column(name = "facility_name")
	private String facilityName;

	@Column(name = "Date", columnDefinition = "DATE")
	private Date date;

	@Column(name = "start_time")
	private Time startTime;

	@Column(name = "end_time")
	private Time endTime;

	@Column(name = "title")
	private String title;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

}
