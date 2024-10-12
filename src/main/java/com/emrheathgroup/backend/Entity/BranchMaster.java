package com.emrheathgroup.backend.Entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EMR_Branch_Master")
public class BranchMaster {

    @Id
    @Column(name = "Branch_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer branchId;

    @Column(name = "Branch_Name")
    private String branchName;

    @CreationTimestamp
    @Column(name = "Created_On")
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "Updated_On")
    private Date updatedOn;

    @Column(name = "Is_Active")
    private Boolean isActive;

    @Column(name = "Is_Deleted")
    private Boolean isDeleted;

}
