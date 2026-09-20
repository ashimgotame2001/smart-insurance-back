package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimPartyRole;
import lombok.*;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimPartyDto {
    private UUID id;
    private ClaimPartyRole role;
    private String name;
    private String contactPhone;
    private String contactEmail;
    private String bankName;
    private String bankAccount;
    private String bankIfsc;
    private UUID referenceId;
}
