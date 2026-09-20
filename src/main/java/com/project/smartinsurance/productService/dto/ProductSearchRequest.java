package com.project.smartinsurance.productService.dto;

import com.project.smartinsurance.commonService.dto.PageFilterRequest;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest extends PageFilterRequest {
    private String code;
    private String name;
    private String category;
    private String lineOfBusiness;
    private String status;
}
