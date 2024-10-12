package com.emrheathgroup.backend.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.DTOs.BranchDTOs.UpdateBranchDto;
import com.emrheathgroup.backend.Entity.BranchMaster;
import com.emrheathgroup.backend.Helper.StaticConstants;
import com.emrheathgroup.backend.Service.AdminService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/createBranch")
    public ResponseEntity<ResponseDTO> createBranch(@RequestBody BranchMaster branchMaster)
    {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO = adminService.createBranch(branchMaster);
        }catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatusCode(500);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setMessage("Exception Occured");
            responseDTO.setStatus(false);
        }finally {
            return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
        }
    }

    @GetMapping("/getAllBranches")
    public ResponseEntity<ResponseDTO> getAllBranch()
    {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO = adminService.getAllBranch();
        }catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatusCode(500);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setMessage("Exception Occured");
            responseDTO.setStatus(false);
        }finally {
            return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
        }
    } 

    @PatchMapping("/updateBranch")
    public ResponseEntity<ResponseDTO> updateBranch(@RequestBody @Valid UpdateBranchDto updateBranchDto)
    {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO = adminService.updateBranch(updateBranchDto);
        }catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatusCode(500);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setMessage("Exception Occured");
            responseDTO.setStatus(false);
        }finally {
            return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
        }
    }

    @DeleteMapping("/deleteBranch")
    public ResponseEntity<ResponseDTO> deleteBranch(@RequestParam Integer branchId)
    {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO = adminService.deleteBranch(branchId);
        }catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatusCode(500);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setMessage("Exception Occured");
            responseDTO.setStatus(false);
        }finally {
            return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
        }
    }

    @GetMapping("/getDoctorSchedule")
    public ResponseEntity<ResponseDTO> getDoctorSchedule(@RequestParam String date, @RequestParam Integer branchId, @RequestParam String specialization)
    {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO = adminService.getDoctorSchedule(date,branchId,specialization);
        }catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatusCode(500);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setMessage("Exception Occured");
            responseDTO.setStatus(false);
        }finally {
            return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
        }
    }

    @PostMapping("/createRole")
    public ResponseEntity<ResponseDTO> createRole(@RequestParam String roleName)
    {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO = adminService.createRole(roleName);
        }catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatusCode(500);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setMessage("Exception Occured");
            responseDTO.setStatus(false);
        }finally {
            return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
        }
    }

    @GetMapping("/getRoles")
    public ResponseEntity<ResponseDTO> getRoles()
    {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            responseDTO = adminService.getRoles();
        }catch (Exception e) {
            e.printStackTrace();
            responseDTO.setStatusCode(500);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setMessage("Exception Occured");
            responseDTO.setStatus(false);
        }finally {
            return new ResponseEntity<>(responseDTO, StaticConstants.statusCodes.get(responseDTO.getStatusCode()));
        }
    }


}
