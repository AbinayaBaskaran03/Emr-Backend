package com.emrheathgroup.backend.DTOs.BranchDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBranchDto {

    @NotNull
    private Integer branchId;
    @NotBlank
    private String branchName;
    @NotNull
    private Boolean isActive;
    @NotNull
    private Boolean isDeleted;

}
