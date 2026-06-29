package com.project.smartinsurance.applicationConfig.mapper;

import com.project.smartinsurance.applicationConfig.dto.AccountingPeriodDto;
import com.project.smartinsurance.applicationConfig.dto.BankBranchDto;
import com.project.smartinsurance.applicationConfig.dto.BankDto;
import com.project.smartinsurance.applicationConfig.dto.BranchDto;
import com.project.smartinsurance.applicationConfig.dto.CommissionSlabDto;
import com.project.smartinsurance.applicationConfig.dto.CountryDto;
import com.project.smartinsurance.applicationConfig.dto.CurrencyDto;
import com.project.smartinsurance.applicationConfig.dto.CurrencyFormatDto;
import com.project.smartinsurance.applicationConfig.dto.DateFormatDto;
import com.project.smartinsurance.applicationConfig.dto.DistrictDto;
import com.project.smartinsurance.applicationConfig.dto.ExchangeRateDto;
import com.project.smartinsurance.applicationConfig.dto.HospitalDto;
import com.project.smartinsurance.applicationConfig.dto.LanguageDto;
import com.project.smartinsurance.applicationConfig.dto.LookupValueDto;
import com.project.smartinsurance.applicationConfig.dto.MunicipalityDto;
import com.project.smartinsurance.applicationConfig.dto.NotificationChannelDto;
import com.project.smartinsurance.applicationConfig.dto.NotificationKeywordDto;
import com.project.smartinsurance.applicationConfig.dto.NotificationTemplateDto;
import com.project.smartinsurance.applicationConfig.dto.ProvinceDto;
import com.project.smartinsurance.applicationConfig.dto.RegionDto;
import com.project.smartinsurance.applicationConfig.dto.ReinsuranceCompanyDto;
import com.project.smartinsurance.applicationConfig.dto.TimeFormatDto;
import com.project.smartinsurance.applicationConfig.dto.UserTypeDto;
import com.project.smartinsurance.applicationConfig.dto.VehicleBrandDto;
import com.project.smartinsurance.applicationConfig.dto.VehicleModelDto;
import com.project.smartinsurance.applicationConfig.dto.WardDto;
import com.project.smartinsurance.applicationConfig.model.AccountingPeriod;
import com.project.smartinsurance.applicationConfig.model.Bank;
import com.project.smartinsurance.applicationConfig.model.BankBranch;
import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.applicationConfig.model.CommissionSlab;
import com.project.smartinsurance.applicationConfig.model.Country;
import com.project.smartinsurance.applicationConfig.model.Currency;
import com.project.smartinsurance.applicationConfig.model.CurrencyFormat;
import com.project.smartinsurance.applicationConfig.model.DateFormat;
import com.project.smartinsurance.applicationConfig.model.District;
import com.project.smartinsurance.applicationConfig.model.ExchangeRate;
import com.project.smartinsurance.applicationConfig.model.Hospital;
import com.project.smartinsurance.applicationConfig.model.Language;
import com.project.smartinsurance.applicationConfig.model.LookupValue;
import com.project.smartinsurance.applicationConfig.model.Municipality;
import com.project.smartinsurance.applicationConfig.model.NotificationChannel;
import com.project.smartinsurance.applicationConfig.model.NotificationKeyword;
import com.project.smartinsurance.applicationConfig.model.NotificationTemplate;
import com.project.smartinsurance.applicationConfig.model.Province;
import com.project.smartinsurance.applicationConfig.model.Region;
import com.project.smartinsurance.applicationConfig.model.ReinsuranceCompany;
import com.project.smartinsurance.applicationConfig.model.TimeFormat;
import com.project.smartinsurance.applicationConfig.model.UserType;
import com.project.smartinsurance.applicationConfig.model.VehicleBrand;
import com.project.smartinsurance.applicationConfig.model.VehicleModel;
import com.project.smartinsurance.applicationConfig.model.Ward;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-29T10:34:25+0545",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class MasterDataMapperImpl implements MasterDataMapper {

    @Override
    public CountryDto toDto(Country country) {
        if ( country == null ) {
            return null;
        }

        CountryDto countryDto = new CountryDto();

        countryDto.setId( country.getId() );
        countryDto.setName( country.getName() );
        countryDto.setCode( country.getCode() );
        countryDto.setStatus( country.getStatus() );

        return countryDto;
    }

    @Override
    public Country toEntity(CountryDto countryDto) {
        if ( countryDto == null ) {
            return null;
        }

        Country.CountryBuilder country = Country.builder();

        country.name( countryDto.getName() );
        country.code( countryDto.getCode() );

        return country.build();
    }

    @Override
    public ProvinceDto toDto(Province province) {
        if ( province == null ) {
            return null;
        }

        ProvinceDto.ProvinceDtoBuilder provinceDto = ProvinceDto.builder();

        provinceDto.countryId( provinceCountryId( province ) );
        provinceDto.country( toRefDto( province.getCountry() ) );
        provinceDto.id( province.getId() );
        provinceDto.name( province.getName() );
        provinceDto.code( province.getCode() );
        provinceDto.status( province.getStatus() );

        return provinceDto.build();
    }

    @Override
    public Province toEntity(ProvinceDto provinceDto) {
        if ( provinceDto == null ) {
            return null;
        }

        Province.ProvinceBuilder province = Province.builder();

        province.country( mapCountryIdToCountry( provinceDto.getCountryId() ) );
        province.name( provinceDto.getName() );
        province.code( provinceDto.getCode() );

        return province.build();
    }

    @Override
    public RegionDto toDto(Region region) {
        if ( region == null ) {
            return null;
        }

        RegionDto.RegionDtoBuilder regionDto = RegionDto.builder();

        regionDto.provinceId( regionProvinceId( region ) );
        regionDto.province( toRefDto( region.getProvince() ) );
        regionDto.countryId( regionCountryId( region ) );
        regionDto.country( toRefDto( region.getCountry() ) );
        regionDto.id( region.getId() );
        regionDto.name( region.getName() );
        regionDto.code( region.getCode() );
        regionDto.status( region.getStatus() );

        return regionDto.build();
    }

    @Override
    public Region toEntity(RegionDto regionDto) {
        if ( regionDto == null ) {
            return null;
        }

        Region.RegionBuilder region = Region.builder();

        region.province( mapProvinceIdToProvince( regionDto.getProvinceId() ) );
        region.country( mapCountryIdToCountry( regionDto.getCountryId() ) );
        region.name( regionDto.getName() );
        region.code( regionDto.getCode() );

        return region.build();
    }

    @Override
    public DistrictDto toDto(District district) {
        if ( district == null ) {
            return null;
        }

        DistrictDto districtDto = new DistrictDto();

        districtDto.setCountryId( districtCountryId( district ) );
        districtDto.setCountry( toRefDto( district.getCountry() ) );
        districtDto.setId( district.getId() );
        districtDto.setCode( district.getCode() );
        districtDto.setName( district.getName() );
        districtDto.setStatus( district.getStatus() );

        return districtDto;
    }

    @Override
    public District toEntity(DistrictDto districtDto) {
        if ( districtDto == null ) {
            return null;
        }

        District.DistrictBuilder district = District.builder();

        district.country( mapCountryIdToCountry( districtDto.getCountryId() ) );
        district.name( districtDto.getName() );
        district.code( districtDto.getCode() );

        return district.build();
    }

    @Override
    public CurrencyDto toDto(Currency currency) {
        if ( currency == null ) {
            return null;
        }

        CurrencyDto currencyDto = new CurrencyDto();

        currencyDto.setCountryId( currencyCountryId( currency ) );
        currencyDto.setCountry( toRefDto( currency.getCountry() ) );
        currencyDto.setId( currency.getId() );
        currencyDto.setName( currency.getName() );
        currencyDto.setCode( currency.getCode() );
        currencyDto.setSymbol( currency.getSymbol() );
        currencyDto.setStatus( currency.getStatus() );

        return currencyDto;
    }

    @Override
    public Currency toEntity(CurrencyDto currencyDto) {
        if ( currencyDto == null ) {
            return null;
        }

        Currency.CurrencyBuilder currency = Currency.builder();

        currency.country( mapCountryIdToCountry( currencyDto.getCountryId() ) );
        currency.name( currencyDto.getName() );
        currency.code( currencyDto.getCode() );
        currency.symbol( currencyDto.getSymbol() );

        return currency.build();
    }

    @Override
    public LanguageDto toDto(Language language) {
        if ( language == null ) {
            return null;
        }

        LanguageDto languageDto = new LanguageDto();

        languageDto.setCountryId( languageCountryId( language ) );
        languageDto.setCountry( toRefDto( language.getCountry() ) );
        languageDto.setId( language.getId() );
        languageDto.setName( language.getName() );
        languageDto.setCode( language.getCode() );
        languageDto.setStatus( language.getStatus() );

        return languageDto;
    }

    @Override
    public Language toEntity(LanguageDto languageDto) {
        if ( languageDto == null ) {
            return null;
        }

        Language.LanguageBuilder language = Language.builder();

        language.country( mapCountryIdToCountry( languageDto.getCountryId() ) );
        language.name( languageDto.getName() );
        language.code( languageDto.getCode() );

        return language.build();
    }

    @Override
    public DateFormatDto toDto(DateFormat dateFormat) {
        if ( dateFormat == null ) {
            return null;
        }

        DateFormatDto dateFormatDto = new DateFormatDto();

        dateFormatDto.setId( dateFormat.getId() );
        dateFormatDto.setFormat( dateFormat.getFormat() );
        dateFormatDto.setDescription( dateFormat.getDescription() );
        dateFormatDto.setStatus( dateFormat.getStatus() );

        return dateFormatDto;
    }

    @Override
    public DateFormat toEntity(DateFormatDto dateFormatDto) {
        if ( dateFormatDto == null ) {
            return null;
        }

        DateFormat.DateFormatBuilder dateFormat = DateFormat.builder();

        dateFormat.format( dateFormatDto.getFormat() );
        dateFormat.description( dateFormatDto.getDescription() );

        return dateFormat.build();
    }

    @Override
    public TimeFormatDto toDto(TimeFormat timeFormat) {
        if ( timeFormat == null ) {
            return null;
        }

        TimeFormatDto timeFormatDto = new TimeFormatDto();

        timeFormatDto.setId( timeFormat.getId() );
        timeFormatDto.setFormat( timeFormat.getFormat() );
        timeFormatDto.setDescription( timeFormat.getDescription() );
        timeFormatDto.setStatus( timeFormat.getStatus() );

        return timeFormatDto;
    }

    @Override
    public TimeFormat toEntity(TimeFormatDto timeFormatDto) {
        if ( timeFormatDto == null ) {
            return null;
        }

        TimeFormat.TimeFormatBuilder timeFormat = TimeFormat.builder();

        timeFormat.format( timeFormatDto.getFormat() );
        timeFormat.description( timeFormatDto.getDescription() );

        return timeFormat.build();
    }

    @Override
    public CurrencyFormatDto toDto(CurrencyFormat currencyFormat) {
        if ( currencyFormat == null ) {
            return null;
        }

        CurrencyFormatDto currencyFormatDto = new CurrencyFormatDto();

        currencyFormatDto.setId( currencyFormat.getId() );
        currencyFormatDto.setLocale( currencyFormat.getLocale() );
        currencyFormatDto.setName( currencyFormat.getName() );
        currencyFormatDto.setDescription( currencyFormat.getDescription() );
        currencyFormatDto.setStatus( currencyFormat.getStatus() );

        return currencyFormatDto;
    }

    @Override
    public CurrencyFormat toEntity(CurrencyFormatDto currencyFormatDto) {
        if ( currencyFormatDto == null ) {
            return null;
        }

        CurrencyFormat.CurrencyFormatBuilder currencyFormat = CurrencyFormat.builder();

        currencyFormat.locale( currencyFormatDto.getLocale() );
        currencyFormat.name( currencyFormatDto.getName() );
        currencyFormat.description( currencyFormatDto.getDescription() );

        return currencyFormat.build();
    }

    @Override
    public UserTypeDto toDto(UserType userType) {
        if ( userType == null ) {
            return null;
        }

        UserTypeDto.UserTypeDtoBuilder userTypeDto = UserTypeDto.builder();

        userTypeDto.id( userType.getId() );
        userTypeDto.name( userType.getName() );
        userTypeDto.code( userType.getCode() );
        userTypeDto.description( userType.getDescription() );
        userTypeDto.status( userType.getStatus() );

        return userTypeDto.build();
    }

    @Override
    public UserType toEntity(UserTypeDto userTypeDto) {
        if ( userTypeDto == null ) {
            return null;
        }

        UserType.UserTypeBuilder userType = UserType.builder();

        userType.name( userTypeDto.getName() );
        userType.code( userTypeDto.getCode() );
        userType.description( userTypeDto.getDescription() );

        return userType.build();
    }

    @Override
    public LookupValueDto toDto(LookupValue lookupValue) {
        if ( lookupValue == null ) {
            return null;
        }

        LookupValueDto lookupValueDto = new LookupValueDto();

        lookupValueDto.setId( lookupValue.getId() );
        lookupValueDto.setCategory( lookupValue.getCategory() );
        lookupValueDto.setCode( lookupValue.getCode() );
        lookupValueDto.setValue( lookupValue.getValue() );
        lookupValueDto.setDescription( lookupValue.getDescription() );
        lookupValueDto.setSortOrder( lookupValue.getSortOrder() );
        lookupValueDto.setStatus( lookupValue.getStatus() );

        return lookupValueDto;
    }

    @Override
    public LookupValue toEntity(LookupValueDto lookupValueDto) {
        if ( lookupValueDto == null ) {
            return null;
        }

        LookupValue.LookupValueBuilder lookupValue = LookupValue.builder();

        lookupValue.category( lookupValueDto.getCategory() );
        lookupValue.code( lookupValueDto.getCode() );
        lookupValue.value( lookupValueDto.getValue() );
        lookupValue.description( lookupValueDto.getDescription() );
        lookupValue.sortOrder( lookupValueDto.getSortOrder() );

        return lookupValue.build();
    }

    @Override
    public HospitalDto toDto(Hospital hospital) {
        if ( hospital == null ) {
            return null;
        }

        HospitalDto hospitalDto = new HospitalDto();

        hospitalDto.setId( hospital.getId() );
        hospitalDto.setName( hospital.getName() );
        hospitalDto.setCode( hospital.getCode() );
        hospitalDto.setCategory( hospital.getCategory() );
        hospitalDto.setAddress( hospital.getAddress() );
        hospitalDto.setCity( hospital.getCity() );
        hospitalDto.setPhone( hospital.getPhone() );
        hospitalDto.setEmail( hospital.getEmail() );
        hospitalDto.setIsNetworkHospital( hospital.getIsNetworkHospital() );
        hospitalDto.setStatus( hospital.getStatus() );

        return hospitalDto;
    }

    @Override
    public Hospital toEntity(HospitalDto hospitalDto) {
        if ( hospitalDto == null ) {
            return null;
        }

        Hospital.HospitalBuilder hospital = Hospital.builder();

        hospital.name( hospitalDto.getName() );
        hospital.code( hospitalDto.getCode() );
        hospital.category( hospitalDto.getCategory() );
        hospital.address( hospitalDto.getAddress() );
        hospital.city( hospitalDto.getCity() );
        hospital.phone( hospitalDto.getPhone() );
        hospital.email( hospitalDto.getEmail() );
        hospital.isNetworkHospital( hospitalDto.getIsNetworkHospital() );

        return hospital.build();
    }

    @Override
    public VehicleBrandDto toDto(VehicleBrand vehicleBrand) {
        if ( vehicleBrand == null ) {
            return null;
        }

        VehicleBrandDto vehicleBrandDto = new VehicleBrandDto();

        vehicleBrandDto.setId( vehicleBrand.getId() );
        vehicleBrandDto.setName( vehicleBrand.getName() );
        vehicleBrandDto.setCode( vehicleBrand.getCode() );
        vehicleBrandDto.setCountry( vehicleBrand.getCountry() );
        vehicleBrandDto.setStatus( vehicleBrand.getStatus() );

        return vehicleBrandDto;
    }

    @Override
    public VehicleBrand toEntity(VehicleBrandDto vehicleBrandDto) {
        if ( vehicleBrandDto == null ) {
            return null;
        }

        VehicleBrand.VehicleBrandBuilder vehicleBrand = VehicleBrand.builder();

        vehicleBrand.name( vehicleBrandDto.getName() );
        vehicleBrand.code( vehicleBrandDto.getCode() );
        vehicleBrand.country( vehicleBrandDto.getCountry() );

        return vehicleBrand.build();
    }

    @Override
    public VehicleModelDto toDto(VehicleModel vehicleModel) {
        if ( vehicleModel == null ) {
            return null;
        }

        VehicleModelDto vehicleModelDto = new VehicleModelDto();

        vehicleModelDto.setBrandId( vehicleModelBrandId( vehicleModel ) );
        vehicleModelDto.setBrand( toRefDto( vehicleModel.getBrand() ) );
        vehicleModelDto.setId( vehicleModel.getId() );
        vehicleModelDto.setName( vehicleModel.getName() );
        vehicleModelDto.setCode( vehicleModel.getCode() );
        vehicleModelDto.setVehicleType( vehicleModel.getVehicleType() );
        vehicleModelDto.setManufacturingYearFrom( vehicleModel.getManufacturingYearFrom() );
        vehicleModelDto.setManufacturingYearTo( vehicleModel.getManufacturingYearTo() );
        vehicleModelDto.setStatus( vehicleModel.getStatus() );

        return vehicleModelDto;
    }

    @Override
    public VehicleModel toEntity(VehicleModelDto vehicleModelDto) {
        if ( vehicleModelDto == null ) {
            return null;
        }

        VehicleModel.VehicleModelBuilder vehicleModel = VehicleModel.builder();

        vehicleModel.brand( mapBrandIdToVehicleBrand( vehicleModelDto.getBrandId() ) );
        vehicleModel.name( vehicleModelDto.getName() );
        vehicleModel.code( vehicleModelDto.getCode() );
        vehicleModel.vehicleType( vehicleModelDto.getVehicleType() );
        vehicleModel.manufacturingYearFrom( vehicleModelDto.getManufacturingYearFrom() );
        vehicleModel.manufacturingYearTo( vehicleModelDto.getManufacturingYearTo() );

        return vehicleModel.build();
    }

    @Override
    public BankDto toDto(Bank bank) {
        if ( bank == null ) {
            return null;
        }

        BankDto bankDto = new BankDto();

        bankDto.setId( bank.getId() );
        bankDto.setName( bank.getName() );
        bankDto.setCode( bank.getCode() );
        bankDto.setSwiftCode( bank.getSwiftCode() );
        bankDto.setBankType( bank.getBankType() );
        bankDto.setStatus( bank.getStatus() );

        return bankDto;
    }

    @Override
    public Bank toEntity(BankDto bankDto) {
        if ( bankDto == null ) {
            return null;
        }

        Bank.BankBuilder bank = Bank.builder();

        bank.name( bankDto.getName() );
        bank.code( bankDto.getCode() );
        bank.swiftCode( bankDto.getSwiftCode() );
        bank.bankType( bankDto.getBankType() );

        return bank.build();
    }

    @Override
    public AccountingPeriodDto toDto(AccountingPeriod accountingPeriod) {
        if ( accountingPeriod == null ) {
            return null;
        }

        AccountingPeriodDto accountingPeriodDto = new AccountingPeriodDto();

        accountingPeriodDto.setId( accountingPeriod.getId() );
        accountingPeriodDto.setName( accountingPeriod.getName() );
        accountingPeriodDto.setCode( accountingPeriod.getCode() );
        accountingPeriodDto.setStartDate( accountingPeriod.getStartDate() );
        accountingPeriodDto.setEndDate( accountingPeriod.getEndDate() );
        accountingPeriodDto.setFiscalYear( accountingPeriod.getFiscalYear() );
        accountingPeriodDto.setIsClosed( accountingPeriod.getIsClosed() );
        accountingPeriodDto.setStatus( accountingPeriod.getStatus() );

        return accountingPeriodDto;
    }

    @Override
    public AccountingPeriod toEntity(AccountingPeriodDto accountingPeriodDto) {
        if ( accountingPeriodDto == null ) {
            return null;
        }

        AccountingPeriod.AccountingPeriodBuilder accountingPeriod = AccountingPeriod.builder();

        accountingPeriod.name( accountingPeriodDto.getName() );
        accountingPeriod.code( accountingPeriodDto.getCode() );
        accountingPeriod.startDate( accountingPeriodDto.getStartDate() );
        accountingPeriod.endDate( accountingPeriodDto.getEndDate() );
        accountingPeriod.fiscalYear( accountingPeriodDto.getFiscalYear() );
        accountingPeriod.isClosed( accountingPeriodDto.getIsClosed() );

        return accountingPeriod.build();
    }

    @Override
    public ReinsuranceCompanyDto toDto(ReinsuranceCompany reinsuranceCompany) {
        if ( reinsuranceCompany == null ) {
            return null;
        }

        ReinsuranceCompanyDto reinsuranceCompanyDto = new ReinsuranceCompanyDto();

        reinsuranceCompanyDto.setId( reinsuranceCompany.getId() );
        reinsuranceCompanyDto.setName( reinsuranceCompany.getName() );
        reinsuranceCompanyDto.setCode( reinsuranceCompany.getCode() );
        reinsuranceCompanyDto.setCountry( reinsuranceCompany.getCountry() );
        reinsuranceCompanyDto.setRating( reinsuranceCompany.getRating() );
        reinsuranceCompanyDto.setContactEmail( reinsuranceCompany.getContactEmail() );
        reinsuranceCompanyDto.setContactPhone( reinsuranceCompany.getContactPhone() );
        reinsuranceCompanyDto.setStatus( reinsuranceCompany.getStatus() );

        return reinsuranceCompanyDto;
    }

    @Override
    public ReinsuranceCompany toEntity(ReinsuranceCompanyDto reinsuranceCompanyDto) {
        if ( reinsuranceCompanyDto == null ) {
            return null;
        }

        ReinsuranceCompany.ReinsuranceCompanyBuilder reinsuranceCompany = ReinsuranceCompany.builder();

        reinsuranceCompany.name( reinsuranceCompanyDto.getName() );
        reinsuranceCompany.code( reinsuranceCompanyDto.getCode() );
        reinsuranceCompany.country( reinsuranceCompanyDto.getCountry() );
        reinsuranceCompany.rating( reinsuranceCompanyDto.getRating() );
        reinsuranceCompany.contactEmail( reinsuranceCompanyDto.getContactEmail() );
        reinsuranceCompany.contactPhone( reinsuranceCompanyDto.getContactPhone() );

        return reinsuranceCompany.build();
    }

    @Override
    public MunicipalityDto toDto(Municipality municipality) {
        if ( municipality == null ) {
            return null;
        }

        MunicipalityDto municipalityDto = new MunicipalityDto();

        municipalityDto.setDistrictId( municipalityDistrictId( municipality ) );
        municipalityDto.setDistrict( toRefDto( municipality.getDistrict() ) );
        municipalityDto.setId( municipality.getId() );
        municipalityDto.setName( municipality.getName() );
        municipalityDto.setCode( municipality.getCode() );
        municipalityDto.setMunicipalityType( municipality.getMunicipalityType() );
        municipalityDto.setStatus( municipality.getStatus() );

        return municipalityDto;
    }

    @Override
    public Municipality toEntity(MunicipalityDto municipalityDto) {
        if ( municipalityDto == null ) {
            return null;
        }

        Municipality.MunicipalityBuilder municipality = Municipality.builder();

        municipality.district( mapDistrictIdToDistrict( municipalityDto.getDistrictId() ) );
        municipality.name( municipalityDto.getName() );
        municipality.code( municipalityDto.getCode() );
        municipality.municipalityType( municipalityDto.getMunicipalityType() );

        return municipality.build();
    }

    @Override
    public WardDto toDto(Ward ward) {
        if ( ward == null ) {
            return null;
        }

        WardDto wardDto = new WardDto();

        wardDto.setMunicipalityId( wardMunicipalityId( ward ) );
        wardDto.setMunicipality( toRefDto( ward.getMunicipality() ) );
        wardDto.setId( ward.getId() );
        wardDto.setName( ward.getName() );
        wardDto.setCode( ward.getCode() );
        wardDto.setWardNumber( ward.getWardNumber() );
        wardDto.setStatus( ward.getStatus() );

        return wardDto;
    }

    @Override
    public Ward toEntity(WardDto wardDto) {
        if ( wardDto == null ) {
            return null;
        }

        Ward.WardBuilder ward = Ward.builder();

        ward.municipality( mapMunicipalityIdToMunicipality( wardDto.getMunicipalityId() ) );
        ward.name( wardDto.getName() );
        ward.code( wardDto.getCode() );
        ward.wardNumber( wardDto.getWardNumber() );

        return ward.build();
    }

    @Override
    public ExchangeRateDto toDto(ExchangeRate exchangeRate) {
        if ( exchangeRate == null ) {
            return null;
        }

        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();

        exchangeRateDto.setId( exchangeRate.getId() );
        exchangeRateDto.setFromCurrency( exchangeRate.getFromCurrency() );
        exchangeRateDto.setToCurrency( exchangeRate.getToCurrency() );
        exchangeRateDto.setRate( exchangeRate.getRate() );
        exchangeRateDto.setEffectiveDate( exchangeRate.getEffectiveDate() );
        exchangeRateDto.setExpiryDate( exchangeRate.getExpiryDate() );
        exchangeRateDto.setStatus( exchangeRate.getStatus() );

        return exchangeRateDto;
    }

    @Override
    public ExchangeRate toEntity(ExchangeRateDto exchangeRateDto) {
        if ( exchangeRateDto == null ) {
            return null;
        }

        ExchangeRate.ExchangeRateBuilder exchangeRate = ExchangeRate.builder();

        exchangeRate.fromCurrency( exchangeRateDto.getFromCurrency() );
        exchangeRate.toCurrency( exchangeRateDto.getToCurrency() );
        exchangeRate.rate( exchangeRateDto.getRate() );
        exchangeRate.effectiveDate( exchangeRateDto.getEffectiveDate() );
        exchangeRate.expiryDate( exchangeRateDto.getExpiryDate() );

        return exchangeRate.build();
    }

    @Override
    public CommissionSlabDto toDto(CommissionSlab commissionSlab) {
        if ( commissionSlab == null ) {
            return null;
        }

        CommissionSlabDto commissionSlabDto = new CommissionSlabDto();

        commissionSlabDto.setId( commissionSlab.getId() );
        commissionSlabDto.setName( commissionSlab.getName() );
        commissionSlabDto.setCode( commissionSlab.getCode() );
        commissionSlabDto.setAgentCategory( commissionSlab.getAgentCategory() );
        commissionSlabDto.setMinAmount( commissionSlab.getMinAmount() );
        commissionSlabDto.setMaxAmount( commissionSlab.getMaxAmount() );
        commissionSlabDto.setCommissionRate( commissionSlab.getCommissionRate() );
        commissionSlabDto.setStatus( commissionSlab.getStatus() );

        return commissionSlabDto;
    }

    @Override
    public CommissionSlab toEntity(CommissionSlabDto commissionSlabDto) {
        if ( commissionSlabDto == null ) {
            return null;
        }

        CommissionSlab.CommissionSlabBuilder commissionSlab = CommissionSlab.builder();

        commissionSlab.name( commissionSlabDto.getName() );
        commissionSlab.code( commissionSlabDto.getCode() );
        commissionSlab.agentCategory( commissionSlabDto.getAgentCategory() );
        commissionSlab.minAmount( commissionSlabDto.getMinAmount() );
        commissionSlab.maxAmount( commissionSlabDto.getMaxAmount() );
        commissionSlab.commissionRate( commissionSlabDto.getCommissionRate() );

        return commissionSlab.build();
    }

    @Override
    public NotificationTemplateDto toDto(NotificationTemplate notificationTemplate) {
        if ( notificationTemplate == null ) {
            return null;
        }

        NotificationTemplateDto notificationTemplateDto = new NotificationTemplateDto();

        notificationTemplateDto.setId( notificationTemplate.getId() );
        notificationTemplateDto.setName( notificationTemplate.getName() );
        notificationTemplateDto.setCode( notificationTemplate.getCode() );
        notificationTemplateDto.setChannel( notificationTemplate.getChannel() );
        notificationTemplateDto.setSubject( notificationTemplate.getSubject() );
        notificationTemplateDto.setBody( notificationTemplate.getBody() );
        notificationTemplateDto.setModule( notificationTemplate.getModule() );
        notificationTemplateDto.setStatus( notificationTemplate.getStatus() );

        return notificationTemplateDto;
    }

    @Override
    public NotificationTemplate toEntity(NotificationTemplateDto notificationTemplateDto) {
        if ( notificationTemplateDto == null ) {
            return null;
        }

        NotificationTemplate.NotificationTemplateBuilder notificationTemplate = NotificationTemplate.builder();

        notificationTemplate.name( notificationTemplateDto.getName() );
        notificationTemplate.code( notificationTemplateDto.getCode() );
        notificationTemplate.channel( notificationTemplateDto.getChannel() );
        notificationTemplate.subject( notificationTemplateDto.getSubject() );
        notificationTemplate.body( notificationTemplateDto.getBody() );
        notificationTemplate.module( notificationTemplateDto.getModule() );

        return notificationTemplate.build();
    }

    @Override
    public NotificationChannelDto toDto(NotificationChannel notificationChannel) {
        if ( notificationChannel == null ) {
            return null;
        }

        NotificationChannelDto.NotificationChannelDtoBuilder notificationChannelDto = NotificationChannelDto.builder();

        notificationChannelDto.id( notificationChannel.getId() );
        notificationChannelDto.name( notificationChannel.getName() );
        notificationChannelDto.code( notificationChannel.getCode() );
        notificationChannelDto.description( notificationChannel.getDescription() );
        notificationChannelDto.status( notificationChannel.getStatus() );

        return notificationChannelDto.build();
    }

    @Override
    public NotificationChannel toEntity(NotificationChannelDto notificationChannelDto) {
        if ( notificationChannelDto == null ) {
            return null;
        }

        NotificationChannel.NotificationChannelBuilder notificationChannel = NotificationChannel.builder();

        notificationChannel.name( notificationChannelDto.getName() );
        notificationChannel.code( notificationChannelDto.getCode() );
        notificationChannel.description( notificationChannelDto.getDescription() );

        return notificationChannel.build();
    }

    @Override
    public NotificationKeywordDto toDto(NotificationKeyword notificationKeyword) {
        if ( notificationKeyword == null ) {
            return null;
        }

        NotificationKeywordDto.NotificationKeywordDtoBuilder notificationKeywordDto = NotificationKeywordDto.builder();

        notificationKeywordDto.id( notificationKeyword.getId() );
        notificationKeywordDto.name( notificationKeyword.getName() );
        notificationKeywordDto.code( notificationKeyword.getCode() );
        notificationKeywordDto.description( notificationKeyword.getDescription() );
        notificationKeywordDto.exampleValue( notificationKeyword.getExampleValue() );
        notificationKeywordDto.status( notificationKeyword.getStatus() );

        return notificationKeywordDto.build();
    }

    @Override
    public NotificationKeyword toEntity(NotificationKeywordDto notificationKeywordDto) {
        if ( notificationKeywordDto == null ) {
            return null;
        }

        NotificationKeyword.NotificationKeywordBuilder notificationKeyword = NotificationKeyword.builder();

        notificationKeyword.name( notificationKeywordDto.getName() );
        notificationKeyword.code( notificationKeywordDto.getCode() );
        notificationKeyword.description( notificationKeywordDto.getDescription() );
        notificationKeyword.exampleValue( notificationKeywordDto.getExampleValue() );

        return notificationKeyword.build();
    }

    @Override
    public BankBranchDto toDto(BankBranch bankBranch) {
        if ( bankBranch == null ) {
            return null;
        }

        BankBranchDto bankBranchDto = new BankBranchDto();

        bankBranchDto.setBankId( bankBranchBankId( bankBranch ) );
        bankBranchDto.setBank( toRefDto( bankBranch.getBank() ) );
        bankBranchDto.setId( bankBranch.getId() );
        bankBranchDto.setName( bankBranch.getName() );
        bankBranchDto.setCode( bankBranch.getCode() );
        bankBranchDto.setAddress( bankBranch.getAddress() );
        bankBranchDto.setCity( bankBranch.getCity() );
        bankBranchDto.setPhone( bankBranch.getPhone() );
        bankBranchDto.setSwiftCode( bankBranch.getSwiftCode() );
        bankBranchDto.setStatus( bankBranch.getStatus() );

        return bankBranchDto;
    }

    @Override
    public BankBranch toEntity(BankBranchDto bankBranchDto) {
        if ( bankBranchDto == null ) {
            return null;
        }

        BankBranch.BankBranchBuilder bankBranch = BankBranch.builder();

        bankBranch.bank( mapBankIdToBank( bankBranchDto.getBankId() ) );
        bankBranch.name( bankBranchDto.getName() );
        bankBranch.code( bankBranchDto.getCode() );
        bankBranch.address( bankBranchDto.getAddress() );
        bankBranch.city( bankBranchDto.getCity() );
        bankBranch.phone( bankBranchDto.getPhone() );
        bankBranch.swiftCode( bankBranchDto.getSwiftCode() );

        return bankBranch.build();
    }

    @Override
    public BranchDto toDto(BranchEntity branch) {
        if ( branch == null ) {
            return null;
        }

        BranchDto.BranchDtoBuilder branchDto = BranchDto.builder();

        branchDto.parentBranchId( branchParentBranchId( branch ) );
        branchDto.id( branch.getId() );
        branchDto.status( branch.getStatus() );
        branchDto.branchCode( branch.getBranchCode() );
        branchDto.branchName( branch.getBranchName() );
        branchDto.branchType( branch.getBranchType() );
        branchDto.branchStatus( branch.getBranchStatus() );
        branchDto.email( branch.getEmail() );
        branchDto.phoneNumber( branch.getPhoneNumber() );
        branchDto.addressLine1( branch.getAddressLine1() );
        branchDto.addressLine2( branch.getAddressLine2() );
        branchDto.city( branch.getCity() );
        branchDto.state( branch.getState() );
        branchDto.country( branch.getCountry() );
        branchDto.postalCode( branch.getPostalCode() );

        return branchDto.build();
    }

    @Override
    public BranchEntity toEntity(BranchDto branchDto) {
        if ( branchDto == null ) {
            return null;
        }

        BranchEntity.BranchEntityBuilder branchEntity = BranchEntity.builder();

        branchEntity.parentBranch( mapParentBranchIdToBranchEntity( branchDto.getParentBranchId() ) );
        branchEntity.branchCode( branchDto.getBranchCode() );
        branchEntity.branchName( branchDto.getBranchName() );
        branchEntity.branchType( branchDto.getBranchType() );
        branchEntity.branchStatus( branchDto.getBranchStatus() );
        branchEntity.email( branchDto.getEmail() );
        branchEntity.phoneNumber( branchDto.getPhoneNumber() );
        branchEntity.addressLine1( branchDto.getAddressLine1() );
        branchEntity.addressLine2( branchDto.getAddressLine2() );
        branchEntity.city( branchDto.getCity() );
        branchEntity.state( branchDto.getState() );
        branchEntity.country( branchDto.getCountry() );
        branchEntity.postalCode( branchDto.getPostalCode() );

        return branchEntity.build();
    }

    private UUID provinceCountryId(Province province) {
        Country country = province.getCountry();
        if ( country == null ) {
            return null;
        }
        return country.getId();
    }

    private UUID regionProvinceId(Region region) {
        Province province = region.getProvince();
        if ( province == null ) {
            return null;
        }
        return province.getId();
    }

    private UUID regionCountryId(Region region) {
        Country country = region.getCountry();
        if ( country == null ) {
            return null;
        }
        return country.getId();
    }

    private UUID districtCountryId(District district) {
        Country country = district.getCountry();
        if ( country == null ) {
            return null;
        }
        return country.getId();
    }

    private UUID currencyCountryId(Currency currency) {
        Country country = currency.getCountry();
        if ( country == null ) {
            return null;
        }
        return country.getId();
    }

    private UUID languageCountryId(Language language) {
        Country country = language.getCountry();
        if ( country == null ) {
            return null;
        }
        return country.getId();
    }

    private UUID vehicleModelBrandId(VehicleModel vehicleModel) {
        VehicleBrand brand = vehicleModel.getBrand();
        if ( brand == null ) {
            return null;
        }
        return brand.getId();
    }

    private UUID municipalityDistrictId(Municipality municipality) {
        District district = municipality.getDistrict();
        if ( district == null ) {
            return null;
        }
        return district.getId();
    }

    private UUID wardMunicipalityId(Ward ward) {
        Municipality municipality = ward.getMunicipality();
        if ( municipality == null ) {
            return null;
        }
        return municipality.getId();
    }

    private UUID bankBranchBankId(BankBranch bankBranch) {
        Bank bank = bankBranch.getBank();
        if ( bank == null ) {
            return null;
        }
        return bank.getId();
    }

    private UUID branchParentBranchId(BranchEntity branchEntity) {
        BranchEntity parentBranch = branchEntity.getParentBranch();
        if ( parentBranch == null ) {
            return null;
        }
        return parentBranch.getId();
    }
}
