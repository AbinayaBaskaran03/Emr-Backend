package com.emrheathgroup.backend.ServiceImpl;

import com.emrheathgroup.backend.DTOs.AdminDTOs.DoctorAppointmentDto;
import com.emrheathgroup.backend.DTOs.AdminDTOs.PatientTimeSlotDto;
import com.emrheathgroup.backend.DTOs.BranchDTOs.UpdateBranchDto;
import com.emrheathgroup.backend.DTOs.ResponseDTO;
import com.emrheathgroup.backend.Entity.*;
import com.emrheathgroup.backend.Repository.BranchRepo;
import com.emrheathgroup.backend.Repository.DoctorRepo;
import com.emrheathgroup.backend.Repository.RoleRepo;
import com.emrheathgroup.backend.Service.AdminService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(rollbackOn = Exception.class)
@Service
public class AdminServiceImpl implements AdminService {


    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
    @Autowired
    private BranchRepo branchRepo;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public ResponseDTO createBranch(BranchMaster branchMaster) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {

            BranchMaster branch = branchRepo.findByBranchName(branchMaster.getBranchName().toLowerCase());

            if (branch != null && branch.getIsActive() && !branch.getIsDeleted())
            {
                log.info("Branch with same name already exists");
                responseDTO.setStatus(false);
                responseDTO.setMessage("Branch already exists");
                responseDTO.setData(new ArrayList<>());
                responseDTO.setStatusCode(409);
                return responseDTO;
            }

            branch = new BranchMaster();
            branch.setBranchName(branchMaster.getBranchName());
            branch.setIsActive(true);
            branch.setIsDeleted(false);

            branchRepo.save(branch);

            responseDTO.setStatusCode(201);
            responseDTO.setMessage("Branch Created Successfully");
            responseDTO.setData(branch);
            responseDTO.setStatus(true);

            log.info("Branch Created Successfully");

        }catch (Exception e) {
            e.printStackTrace();
            log.error("Exception Occured: " + e);
            responseDTO.setStatus(false);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setStatusCode(500);
            responseDTO.setMessage("Exception Occured");
        }finally {
            return responseDTO;
        }
    }

    @Override
    public ResponseDTO getAllBranch() {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            List<BranchMaster> branchMasters = branchRepo.findAll();

            responseDTO.setStatus(true);
            responseDTO.setData(branchMasters);
            responseDTO.setMessage("All Branches Fetched Successfully");
            responseDTO.setStatusCode(200);

            log.info("All Branch List Fetched");

        }catch (Exception e) {
            e.printStackTrace();
            log.error("Exception Occured: " + e);
            responseDTO.setStatus(false);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setStatusCode(500);
            responseDTO.setMessage("Exception Occured");
        }finally {
            return responseDTO;
        }
    }

    @Override
    public ResponseDTO updateBranch(UpdateBranchDto updateBranchDto) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            BranchMaster branchMaster = branchRepo.findById(updateBranchDto.getBranchId()).orElse(null);

            if (branchMaster == null)
            {
                log.info("Branch Not Found");
                responseDTO.setStatus(false);
                responseDTO.setMessage("Branch Not Found");
                responseDTO.setData(new ArrayList<>());
                responseDTO.setStatusCode(404);
                return responseDTO;
            }

            branchMaster.setBranchName(updateBranchDto.getBranchName());
            branchMaster.setIsActive(updateBranchDto.getIsActive());
            branchMaster.setIsDeleted(updateBranchDto.getIsDeleted());

            branchRepo.save(branchMaster);

            responseDTO.setStatus(true);
            responseDTO.setData(branchMaster);
            responseDTO.setMessage("Branch Updated Successfully");
            responseDTO.setStatusCode(200);

            log.info("Branch Updated Successfully");

        }catch (Exception e) {
            e.printStackTrace();
            log.error("Exception Occured: " + e);
            responseDTO.setStatus(false);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setStatusCode(500);
            responseDTO.setMessage("Exception Occured");
        }finally {
            return responseDTO;
        }
    }

    @Override
    public ResponseDTO deleteBranch(Integer branchId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            BranchMaster branchMaster = branchRepo.findById(branchId).orElse(null);

            if (branchMaster == null)
            {
                log.info("Branch Not Found");
                responseDTO.setStatus(false);
                responseDTO.setMessage("Branch Not Found");
                responseDTO.setData(new ArrayList<>());
                responseDTO.setStatusCode(404);
                return responseDTO;
            }

            branchMaster.setIsActive(false);
            branchMaster.setIsDeleted(true);

            branchRepo.save(branchMaster);

            responseDTO.setStatus(true);
            responseDTO.setData(branchMaster);
            responseDTO.setMessage("Branch Updated Successfully");
            responseDTO.setStatusCode(200);

            log.info("Branch Updated Successfully");

        }catch (Exception e) {
            e.printStackTrace();
            log.error("Exception Occured: " + e);
            responseDTO.setStatus(false);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setStatusCode(500);
            responseDTO.setMessage("Exception Occured");
        }finally {
            return responseDTO;
        }
    }

    @Override
    public ResponseDTO getDoctorSchedule(String date, Integer branchId, String specialization) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date checkDate = dateFormat.parse(date);

            BranchMaster branch = branchRepo.findById(branchId).orElse(null);

            if (branch == null) {
                log.info("Branch Not Found");
                responseDTO.setStatus(false);
                responseDTO.setMessage("Branch Not Found");
                responseDTO.setData(new ArrayList<>());
                responseDTO.setStatusCode(404);
                return responseDTO;
            }


            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            //find roaster for the doctors of that branch
            CriteriaQuery<DoctorRoster> doctorRosterCriteriaQuery = criteriaBuilder.createQuery(DoctorRoster.class);
            Root<DoctorRoster> doctorRosterRoot = doctorRosterCriteriaQuery.from(DoctorRoster.class);

            List<Predicate> roasterPredicates = new ArrayList<>();
            roasterPredicates.add(criteriaBuilder.equal(doctorRosterRoot.get("date"), checkDate));
            roasterPredicates.add(criteriaBuilder.equal(doctorRosterRoot.get("branchId"), branch));
            roasterPredicates.add(criteriaBuilder.equal(doctorRosterRoot.get("doctorId").get("specialization"), specialization));

            doctorRosterCriteriaQuery.select(doctorRosterRoot).where(roasterPredicates.toArray(new Predicate[]{}));
            List<DoctorRoster> doctorRosters = entityManager.createQuery(doctorRosterCriteriaQuery).getResultList();

            List<Doctor> doctorList = doctorRosters.stream().map(doctorRoster -> doctorRoster.getDoctor()).collect(Collectors.toList());

            if (doctorRosters.isEmpty())
            {
                log.info("No Roaster Available");
                responseDTO.setStatus(false);
                responseDTO.setMessage("No Roaster Found");
                responseDTO.setData(new ArrayList<>());
                responseDTO.setStatusCode(404);
                return responseDTO;
            }

            CriteriaQuery<DoctorAppointment> doctorAppointmentCriteriaQuery = criteriaBuilder.createQuery(DoctorAppointment.class);
            Root<DoctorAppointment> doctorAppointmentRoot = doctorAppointmentCriteriaQuery.from(DoctorAppointment.class);

            List<Predicate> appointmentPredicates = new ArrayList<>();
            appointmentPredicates.add(criteriaBuilder.equal(doctorAppointmentRoot.get("appointmentDate"),checkDate));
            appointmentPredicates.add(doctorAppointmentRoot.get("doctorId").in(doctorList));
            appointmentPredicates.add(criteriaBuilder.equal(doctorAppointmentRoot.get("branchMaster"),branch));

            doctorAppointmentCriteriaQuery.select(doctorAppointmentRoot).where(appointmentPredicates.toArray(new Predicate[]{}));
            TypedQuery<DoctorAppointment> doctorAppointmentQuery = entityManager.createQuery(doctorAppointmentCriteriaQuery);
            List<DoctorAppointment> doctorAppointments = doctorAppointmentQuery.getResultList();

            List<DoctorAppointmentDto> appointmentDtos = new ArrayList<>();

            for (DoctorRoster doctor : doctorRosters)
            {
                DoctorAppointmentDto dto = new DoctorAppointmentDto();

                dto.setDocId(doctor.getDoctor().getDoctorId());
                dto.setDocName(doctor.getDoctor().getName());
               // dto.setSpecialization(doctor.getDoctorId().getSpeciality());
                dto.setBranchId(doctor.getBranchMaster().getBranchId());
                dto.setStartTime(doctor.getStartTime());
                dto.setEndTime(doctor.getEndTime());


                List<DoctorAppointment> appointments = doctorAppointments.stream().filter(doctorAppointment -> doctorAppointment.getDoctor().getDoctorId().equals(doctor)).collect(Collectors.toList());
                List<PatientTimeSlotDto> patientTimeSlotDtos = new ArrayList<>();
                if (!appointments.isEmpty())
                {
                    for (DoctorAppointment doctorAppointment : appointments)
                    {
                        PatientTimeSlotDto patientTimeSlotDto = new PatientTimeSlotDto(
                            doctorAppointment.getPatient().getName(),
                                doctorAppointment.getAppointmentTime(),
                                doctorAppointment.getPatient().getPatientId().intValue()
                        );

                        patientTimeSlotDtos.add(patientTimeSlotDto);
                    }

                }

                dto.setAppointments(patientTimeSlotDtos);

                appointmentDtos.add(dto);
            }

            responseDTO.setStatus(true);
            responseDTO.setData(appointmentDtos);
            responseDTO.setMessage("Schedule Fetched Successfully");
            responseDTO.setStatusCode(200);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception Occured: " + e);
            responseDTO.setStatus(false);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setStatusCode(500);
            responseDTO.setMessage("Exception Occured");
        } finally {
            return responseDTO;
        }
    }

    @Override
    public ResponseDTO createRole(String roleName) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {


            RoleMaster roleMaster = roleRepo.findByRoleName(roleName.toLowerCase());

            if (roleMaster != null)
            {
                responseDTO.setStatus(false);
                responseDTO.setData(new ArrayList<>());
                responseDTO.setStatusCode(409);
                responseDTO.setMessage("Role Already Exists");
                return responseDTO;
            }

            roleMaster = new RoleMaster();
            roleMaster.setRoleName(roleName);

            roleRepo.save(roleMaster);

            responseDTO.setStatus(true);
            responseDTO.setData(roleMaster);
            responseDTO.setMessage("Role Created Successfully");
            responseDTO.setStatusCode(200);

        }catch (Exception e) {
            e.printStackTrace();
            log.error("Exception Occured: " + e);
            responseDTO.setStatus(false);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setStatusCode(500);
            responseDTO.setMessage("Exception Occured");
        }finally {
            return responseDTO;
        }
    }

    @Override
    public ResponseDTO getRoles() {

        ResponseDTO responseDTO = new ResponseDTO();

        try {

            List<RoleMaster> roleMasters = roleRepo.findAll();

            responseDTO.setStatusCode(200);
            responseDTO.setStatus(true);
            responseDTO.setData(roleMasters);
            responseDTO.setMessage("Roles List Fetched Successfully");

        }catch (Exception e) {
            e.printStackTrace();
            log.error("Exception Occured: " + e);
            responseDTO.setStatus(false);
            responseDTO.setData(new ArrayList<>());
            responseDTO.setStatusCode(500);
            responseDTO.setMessage("Exception Occured");
        }finally {
            return responseDTO;
        }
    }


}
