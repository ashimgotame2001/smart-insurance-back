package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.agentService.model.enums.AddressType;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "agent_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentAddress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    private AddressType addressType;

    @Column(name = "province_id")
    private UUID provinceId;

    @Column(name = "district_id")
    private UUID districtId;

    @Column(name = "municipality_id")
    private UUID municipalityId;

    @Column(name = "ward_id")
    private UUID wardId;

    @Column(name = "street")
    private String street;

    @Column(name = "postal_code")
    private String postalCode;

    @Builder.Default
    @Column(name = "same_as_permanent")
    private Boolean sameAsPermanent = false;
}
