package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDto {
    private UUID id;
    private String name;
    private String code;
    private UUID provinceId;
    private RefDto province;
    private UUID countryId;
    private RefDto country;
    private Status status;
}
