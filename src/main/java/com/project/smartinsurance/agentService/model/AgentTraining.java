package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "agent_trainings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentTraining extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "training_name")
    private String trainingName;

    @Column(name = "training_provider")
    private String trainingProvider;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "certificate_number")
    private String certificateNumber;

    @Column(name = "grade")
    private String grade;

    @Column(name = "remarks")
    private String remarks;
}
