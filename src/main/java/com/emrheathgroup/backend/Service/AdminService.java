package com.emrheathgroup.backend.Service;

import org.springframework.stereotype.Component;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.BranchDTOs.UpdateBranchDto;
import com.emrheathgroup.backend.Entity.BranchMaster;

@Component
public interface AdminService {
    ResponseDTO createBranch(BranchMaster branchMaster);

    ResponseDTO getAllBranch();

    ResponseDTO deleteBranch(Integer branchId);

    ResponseDTO updateBranch(UpdateBranchDto updateBranchDto);


    ResponseDTO getDoctorSchedule(String date, Integer branchId, String specialization);

    ResponseDTO createRole(String roleName);

    ResponseDTO getRoles();
}
