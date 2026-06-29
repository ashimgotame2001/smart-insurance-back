package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.CountryDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CountryService {
    CountryDto createCountry(CountryDto countryDto);
    CountryDto updateCountry(UUID id, CountryDto countryDto);
    CountryDto getCountryById(UUID id);
    List<CountryDto> getAllCountries();
    void deleteCountry(UUID id);
}
