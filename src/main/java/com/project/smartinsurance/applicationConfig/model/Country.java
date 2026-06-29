package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "master_countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<District> districts;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<Currency> currencies;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<Language> languages;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<Province> provinces;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<Region> regions;
}
