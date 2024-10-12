package com.emrheathgroup.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {

    private Boolean status;
    private String message;
    private Object data;
    private Integer statusCode;

    
}



 