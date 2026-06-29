package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class HospitalDto {
    private UUID id;
    private String name;
    private String code;
    private String category;
    private String address;
    private String city;
    private String phone;
    private String email;
    private Boolean isNetworkHospital;
    private Status status;
}
