package com.project.smartinsurance.applicationConfig.validation;

import com.project.smartinsurance.applicationConfig.dto.CountryDto;
import com.project.smartinsurance.applicationConfig.dto.UserTypeDto;
import com.project.smartinsurance.applicationConfig.dto.NotificationChannelDto;
import com.project.smartinsurance.applicationConfig.repository.*;
import com.project.smartinsurance.commonService.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MasterDataValidationService {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final LanguageRepository languageRepository;
    private final DateFormatRepository dateFormatRepository;
    private final TimeFormatRepository timeFormatRepository;
    private final UserTypeRepository userTypeRepository;
    private final ProvinceRepository provinceRepository;
    private final RegionRepository regionRepository;
    private final LookupValueRepository lookupValueRepository;
    private final HospitalRepository hospitalRepository;
    private final VehicleBrandRepository vehicleBrandRepository;
    private final VehicleModelRepository vehicleModelRepository;
    private final BankRepository bankRepository;
    private final AccountingPeriodRepository accountingPeriodRepository;
    private final ReinsuranceCompanyRepository reinsuranceCompanyRepository;
    private final NotificationChannelRepository notificationChannelRepository;
    private final CurrencyFormatRepository currencyFormatRepository;

    public void validateUniqueCode(JpaRepository<?, UUID> repository, String code) {
        // Delegate to specific repo checks based on instance
        if (repository instanceof HospitalRepository r && r.existsByCode(code)) throw new GlobalException("HSP-002");
        if (repository instanceof VehicleBrandRepository r && r.existsByCode(code)) throw new GlobalException("VBR-002");
        if (repository instanceof VehicleModelRepository r && r.existsByCode(code)) throw new GlobalException("VMO-002");
        if (repository instanceof BankRepository r && r.existsByCode(code)) throw new GlobalException("BNK-002");
        if (repository instanceof AccountingPeriodRepository r && r.existsByCode(code)) throw new GlobalException("ACP-002");
        if (repository instanceof ReinsuranceCompanyRepository r && r.existsByCode(code)) throw new GlobalException("RIN-002");
        if (repository instanceof WardRepository r && r.existsByCode(code)) throw new GlobalException("WRD-002");
        if (repository instanceof MunicipalityRepository r && r.existsByCode(code)) throw new GlobalException("MUN-002");
        if (repository instanceof DistrictRepository r && r.existsByCode(code)) throw new GlobalException("DST-003");
        if (repository instanceof BranchRepository r && r.existsByBranchCode(code)) throw new GlobalException("BRN-003");
        if (repository instanceof BankBranchRepository r && r.existsByCode(code)) throw new GlobalException("BKB-002");
    }

    public void validateLookupValue(String category, String code) {
        if (lookupValueRepository.existsByCategoryAndCode(category, code)) {
            throw new GlobalException("SYS-003");
        }
    }

    public void validateCountry(CountryDto countryDto) {
        if (countryRepository.existsByName(countryDto.getName())) throw new GlobalException("CTY-002");
        if (countryRepository.existsByCode(countryDto.getCode())) throw new GlobalException("CTY-003");
    }

    public void validateCurrency(String code) {
        if (currencyRepository.existsByCode(code)) throw new GlobalException("CUR-002");
    }

    public void validateLanguage(String code) {
        if (languageRepository.existsByCode(code)) throw new GlobalException("LNG-002");
    }

    public void validateDateFormat(String format) {
        if (dateFormatRepository.existsByFormat(format)) throw new GlobalException("DAT-002");
    }

    public void validateTimeFormat(String format) {
        if (timeFormatRepository.existsByFormat(format)) throw new GlobalException("TIM-002");
    }

    public void validateUserType(UserTypeDto userTypeDto) {
        if (userTypeRepository.existsByName(userTypeDto.getName())) throw new GlobalException("UTP-002");
        if (userTypeRepository.existsByCode(userTypeDto.getCode())) throw new GlobalException("UTP-003");
    }

    public void validateProvince(String code) {
        if (provinceRepository.existsByCode(code)) throw new GlobalException("PRV-003");
    }

    public void validateRegion(String code) {
        if (regionRepository.existsByCode(code)) throw new GlobalException("RGN-002");
    }

    public void validateCountryExists(UUID id) {
        if (id != null && !countryRepository.existsById(id)) throw new GlobalException("CTY-001");
    }

    public void validateProvinceExists(UUID id) {
        if (id != null && !provinceRepository.existsById(id)) throw new GlobalException("PRV-001");
    }

    public void validateVehicleBrandExists(UUID id) {
        if (id != null && !vehicleBrandRepository.existsById(id)) throw new GlobalException("VBR-001");
    }

    public void validateNotificationChannel(NotificationChannelDto dto) {
        if (notificationChannelRepository.existsByName(dto.getName())) throw new GlobalException("NTT-002");
        if (notificationChannelRepository.existsByCode(dto.getCode())) throw new GlobalException("NTT-003");
    }

    public void validateCurrencyFormat(String locale) {
        if (currencyFormatRepository.existsByLocale(locale)) throw new GlobalException("CRF-002");
    }
}
