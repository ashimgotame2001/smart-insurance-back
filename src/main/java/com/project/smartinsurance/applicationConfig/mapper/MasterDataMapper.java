package com.project.smartinsurance.applicationConfig.mapper;

import com.project.smartinsurance.applicationConfig.dto.*;
import com.project.smartinsurance.applicationConfig.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MasterDataMapper {

    CountryDto toDto(Country country);
    Country toEntity(CountryDto countryDto);

    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "country", source = "country")
    ProvinceDto toDto(Province province);
    @Mapping(target = "country", source = "countryId")
    Province toEntity(ProvinceDto provinceDto);

    @Mapping(target = "provinceId", source = "province.id")
    @Mapping(target = "province", source = "province")
    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "country", source = "country")
    RegionDto toDto(Region region);
    @Mapping(target = "province", source = "provinceId")
    @Mapping(target = "country", source = "countryId")
    Region toEntity(RegionDto regionDto);

    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "country", source = "country")
    DistrictDto toDto(District district);
    @Mapping(target = "country", source = "countryId")
    District toEntity(DistrictDto districtDto);

    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "country", source = "country")
    CurrencyDto toDto(Currency currency);
    @Mapping(target = "country", source = "countryId")
    Currency toEntity(CurrencyDto currencyDto);

    @Mapping(target = "countryId", source = "country.id")
    @Mapping(target = "country", source = "country")
    LanguageDto toDto(Language language);
    @Mapping(target = "country", source = "countryId")
    Language toEntity(LanguageDto languageDto);

    DateFormatDto toDto(DateFormat dateFormat);
    DateFormat toEntity(DateFormatDto dateFormatDto);

    TimeFormatDto toDto(TimeFormat timeFormat);
    TimeFormat toEntity(TimeFormatDto timeFormatDto);

    CurrencyFormatDto toDto(CurrencyFormat currencyFormat);
    CurrencyFormat toEntity(CurrencyFormatDto currencyFormatDto);

    UserTypeDto toDto(UserType userType);
    UserType toEntity(UserTypeDto userTypeDto);

    LookupValueDto toDto(LookupValue lookupValue);
    LookupValue toEntity(LookupValueDto lookupValueDto);

    HospitalDto toDto(Hospital hospital);
    Hospital toEntity(HospitalDto hospitalDto);

    VehicleBrandDto toDto(VehicleBrand vehicleBrand);
    VehicleBrand toEntity(VehicleBrandDto vehicleBrandDto);

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "brand", source = "brand")
    VehicleModelDto toDto(VehicleModel vehicleModel);
    @Mapping(target = "brand", source = "brandId")
    VehicleModel toEntity(VehicleModelDto vehicleModelDto);

    BankDto toDto(Bank bank);
    Bank toEntity(BankDto bankDto);

    AccountingPeriodDto toDto(AccountingPeriod accountingPeriod);
    AccountingPeriod toEntity(AccountingPeriodDto accountingPeriodDto);

    ReinsuranceCompanyDto toDto(ReinsuranceCompany reinsuranceCompany);
    ReinsuranceCompany toEntity(ReinsuranceCompanyDto reinsuranceCompanyDto);

    @Mapping(target = "districtId", source = "district.id")
    @Mapping(target = "district", source = "district")
    MunicipalityDto toDto(Municipality municipality);
    @Mapping(target = "district", source = "districtId")
    Municipality toEntity(MunicipalityDto municipalityDto);

    @Mapping(target = "municipalityId", source = "municipality.id")
    @Mapping(target = "municipality", source = "municipality")
    WardDto toDto(Ward ward);
    @Mapping(target = "municipality", source = "municipalityId")
    Ward toEntity(WardDto wardDto);

    ExchangeRateDto toDto(ExchangeRate exchangeRate);
    ExchangeRate toEntity(ExchangeRateDto exchangeRateDto);

    CommissionSlabDto toDto(CommissionSlab commissionSlab);
    CommissionSlab toEntity(CommissionSlabDto commissionSlabDto);

    NotificationTemplateDto toDto(NotificationTemplate notificationTemplate);
    NotificationTemplate toEntity(NotificationTemplateDto notificationTemplateDto);

    NotificationChannelDto toDto(NotificationChannel notificationChannel);
    NotificationChannel toEntity(NotificationChannelDto notificationChannelDto);

    NotificationKeywordDto toDto(NotificationKeyword notificationKeyword);
    NotificationKeyword toEntity(NotificationKeywordDto notificationKeywordDto);

    @Mapping(target = "bankId", source = "bank.id")
    @Mapping(target = "bank", source = "bank")
    BankBranchDto toDto(BankBranch bankBranch);
    @Mapping(target = "bank", source = "bankId")
    BankBranch toEntity(BankBranchDto bankBranchDto);

    @Mapping(target = "parentBranchId", source = "parentBranch.id")
    BranchDto toDto(BranchEntity branch);
    @Mapping(target = "parentBranch", source = "parentBranchId")
    BranchEntity toEntity(BranchDto branchDto);

    // RefDto mappings
    default RefDto toRefDto(Country country) {
        if (country == null) return null;
        return RefDto.builder().id(country.getId()).name(country.getName()).build();
    }

    default RefDto toRefDto(Province province) {
        if (province == null) return null;
        return RefDto.builder().id(province.getId()).name(province.getName()).build();
    }

    default RefDto toRefDto(District district) {
        if (district == null) return null;
        return RefDto.builder().id(district.getId()).name(district.getName()).build();
    }

    default RefDto toRefDto(Municipality municipality) {
        if (municipality == null) return null;
        return RefDto.builder().id(municipality.getId()).name(municipality.getName()).build();
    }

    default RefDto toRefDto(Bank bank) {
        if (bank == null) return null;
        return RefDto.builder().id(bank.getId()).name(bank.getName()).build();
    }

    default RefDto toRefDto(VehicleBrand brand) {
        if (brand == null) return null;
        return RefDto.builder().id(brand.getId()).name(brand.getName()).build();
    }

    default Country mapCountryIdToCountry(java.util.UUID countryId) {
        if (countryId == null) return null;
        Country country = new Country();
        country.setId(countryId);
        return country;
    }

    default Province mapProvinceIdToProvince(java.util.UUID provinceId) {
        if (provinceId == null) return null;
        Province province = new Province();
        province.setId(provinceId);
        return province;
    }

    default VehicleBrand mapBrandIdToVehicleBrand(java.util.UUID brandId) {
        if (brandId == null) return null;
        VehicleBrand brand = new VehicleBrand();
        brand.setId(brandId);
        return brand;
    }

    default BranchEntity mapParentBranchIdToBranchEntity(java.util.UUID parentBranchId) {
        if (parentBranchId == null) return null;
        BranchEntity branch = new BranchEntity();
        branch.setId(parentBranchId);
        return branch;
    }

    default District mapDistrictIdToDistrict(java.util.UUID districtId) {
        if (districtId == null) return null;
        District district = new District();
        district.setId(districtId);
        return district;
    }

    default Municipality mapMunicipalityIdToMunicipality(java.util.UUID municipalityId) {
        if (municipalityId == null) return null;
        Municipality municipality = new Municipality();
        municipality.setId(municipalityId);
        return municipality;
    }

    default Bank mapBankIdToBank(java.util.UUID bankId) {
        if (bankId == null) return null;
        Bank bank = new Bank();
        bank.setId(bankId);
        return bank;
    }
}
