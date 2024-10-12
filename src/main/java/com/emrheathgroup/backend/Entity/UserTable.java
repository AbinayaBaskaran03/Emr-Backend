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
@Table(name = "User_Table")
public class UserTable {

	@Id
	@Column(name = "User_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Username", unique = true, nullable = false)
	private String userName;

	@Column(name = "Email", unique = true, nullable = false)
	private String email;

	@Column(name = "Phone_No", unique = true, nullable = false)
	private String phoneNo;

	@Column(name = "Password", nullable = false)
	private String password;

	@Column(name = "Reset_Token", unique = true)
	private String resetToken;

	@ManyToOne
	@JoinColumn(name = "Role_Id")
	private RoleMaster roleMaster;

	@ManyToOne
	@JoinColumn(name = "Branch_Id")
	private BranchMaster branchMaster;

	
}
