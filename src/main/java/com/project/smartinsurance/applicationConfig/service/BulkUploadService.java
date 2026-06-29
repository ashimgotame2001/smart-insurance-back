package com.project.smartinsurance.applicationConfig.service;

import com.opencsv.CSVReader;
import com.project.smartinsurance.applicationConfig.dto.*;
import com.project.smartinsurance.commonService.dto.BulkUploadResult;
import com.project.smartinsurance.commonService.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BulkUploadService {

    private final CountryService countryService;
    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final CurrencyService currencyService;
    private final LanguageService languageService;
    private final DateFormatService dateFormatService;
    private final TimeFormatService timeFormatService;
    private final RegionService regionService;
    private final UserTypeService userTypeService;
    private final MunicipalityService municipalityService;
    private final WardService wardService;
    private final BankService bankService;
    private final BankBranchService bankBranchService;
    private final HospitalService hospitalService;
    private final VehicleBrandService vehicleBrandService;
    private final VehicleModelService vehicleModelService;
    private final CommissionSlabService commissionSlabService;
    private final ExchangeRateService exchangeRateService;
    private final LookupValueService lookupValueService;
    private final NotificationChannelService notificationChannelService;
    private final NotificationKeywordService notificationKeywordService;
    private final NotificationTemplateService notificationTemplateService;
    private final ReinsuranceCompanyService reinsuranceCompanyService;

    private static final Map<String, String[]> COLUMN_MAP = new LinkedHashMap<>();

    static {
        COLUMN_MAP.put("countries", new String[]{"code", "name"});
        COLUMN_MAP.put("provinces", new String[]{"code", "name", "countryId"});
        COLUMN_MAP.put("districts", new String[]{"code", "name", "countryId"});
        COLUMN_MAP.put("currencies", new String[]{"code", "name", "symbol", "countryId"});
        COLUMN_MAP.put("languages", new String[]{"code", "name", "countryId"});
        COLUMN_MAP.put("dateFormats", new String[]{"format", "description"});
        COLUMN_MAP.put("timeFormats", new String[]{"format", "description"});
        COLUMN_MAP.put("regions", new String[]{"code", "name", "provinceId", "countryId"});
        COLUMN_MAP.put("userTypes", new String[]{"code", "name", "description"});
        COLUMN_MAP.put("municipalities", new String[]{"code", "name", "municipalityType", "districtId"});
        COLUMN_MAP.put("wards", new String[]{"code", "name", "wardNumber", "municipalityId"});
        COLUMN_MAP.put("banks", new String[]{"code", "name"});
        COLUMN_MAP.put("bankBranches", new String[]{"code", "name", "address", "bankId"});
        COLUMN_MAP.put("hospitals", new String[]{"code", "name", "address", "phoneNumber"});
        COLUMN_MAP.put("vehicleBrands", new String[]{"code", "name"});
        COLUMN_MAP.put("vehicleModels", new String[]{"code", "name", "brandId"});
        COLUMN_MAP.put("commissionSlabs", new String[]{"code", "name", "agentCategory", "minAmount", "maxAmount", "commissionRate"});
        COLUMN_MAP.put("exchangeRates", new String[]{"fromCurrency", "toCurrency", "rate", "effectiveDate", "expiryDate"});
        COLUMN_MAP.put("lookupValues", new String[]{"category", "code", "value", "description", "sortOrder"});
        COLUMN_MAP.put("notificationChannels", new String[]{"code", "name", "description"});
        COLUMN_MAP.put("notificationKeywords", new String[]{"code", "name", "exampleValue", "description"});
        COLUMN_MAP.put("notificationTemplates", new String[]{"code", "name", "channel", "subject", "body", "module"});
        COLUMN_MAP.put("reinsuranceCompanies", new String[]{"code", "name", "country", "rating", "contactEmail", "contactPhone"});
    }

    public BulkUploadResult processUpload(String type, MultipartFile file) {
        BulkUploadResult result = new BulkUploadResult();
        result.setTotalRows(0);
        result.setSuccessCount(0);
        result.setErrorCount(0);

        String[] expectedColumns = COLUMN_MAP.get(type);
        if (expectedColumns == null) {
            result.addError(0, "Unknown entity type: " + type);
            return result;
        }

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            String[] header = csvReader.readNext();
            if (header == null) {
                result.addError(0, "Empty CSV file");
                return result;
            }

            String[] row;
            int rowNum = 1;
            while ((row = csvReader.readNext()) != null) {
                rowNum++;
                result.setTotalRows(result.getTotalRows() + 1);
                try {
                    processRow(type, header, row);
                    result.setSuccessCount(result.getSuccessCount() + 1);
                } catch (Exception e) {
                    result.addError(rowNum, e.getMessage() != null ? e.getMessage() : "Processing failed");
                }
            }
        } catch (Exception e) {
            result.addError(0, "Failed to read file: " + e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void processRow(String type, String[] header, String[] row) {
        Map<String, String> values = new HashMap<>();
        for (int i = 0; i < header.length && i < row.length; i++) {
            String key = header[i].trim();
            String val = row[i] == null ? "" : row[i].trim();
            if (!val.isEmpty()) values.put(key, val);
        }

        switch (type) {
            case "countries" -> countryService.createCountry(bind(CountryDto::new, values, "code", "name"));
            case "provinces" -> provinceService.createProvince(bind(ProvinceDto::new, values, "code", "name", "countryId"));
            case "districts" -> districtService.createDistrict(bind(DistrictDto::new, values, "code", "name", "countryId"));
            case "currencies" -> currencyService.createCurrency(bind(CurrencyDto::new, values, "code", "name", "symbol", "countryId"));
            case "languages" -> languageService.createLanguage(bind(LanguageDto::new, values, "code", "name", "countryId"));
            case "dateFormats" -> dateFormatService.createDateFormat(bind(DateFormatDto::new, values, "format", "description"));
            case "timeFormats" -> timeFormatService.createTimeFormat(bind(TimeFormatDto::new, values, "format", "description"));
            case "regions" -> regionService.createRegion(bind(RegionDto::new, values, "code", "name", "provinceId", "countryId"));
            case "userTypes" -> userTypeService.createUserType(bind(UserTypeDto::new, values, "code", "name", "description"));
            case "municipalities" -> municipalityService.createMunicipality(bind(MunicipalityDto::new, values, "code", "name", "municipalityType", "districtId"));
            case "wards" -> wardService.createWard(bind(WardDto::new, values, "code", "name", "wardNumber", "municipalityId"));
            case "banks" -> bankService.createBank(bind(BankDto::new, values, "code", "name"));
            case "bankBranches" -> bankBranchService.createBankBranch(bind(BankBranchDto::new, values, "code", "name", "address", "bankId"));
            case "hospitals" -> hospitalService.createHospital(bind(HospitalDto::new, values, "code", "name", "address", "phoneNumber"));
            case "vehicleBrands" -> vehicleBrandService.createVehicleBrand(bind(VehicleBrandDto::new, values, "code", "name"));
            case "vehicleModels" -> vehicleModelService.createVehicleModel(bind(VehicleModelDto::new, values, "code", "name", "brandId"));
            case "commissionSlabs" -> commissionSlabService.createCommissionSlab(bind(CommissionSlabDto::new, values, "code", "name", "agentCategory", "minAmount", "maxAmount", "commissionRate"));
            case "exchangeRates" -> exchangeRateService.createExchangeRate(bind(ExchangeRateDto::new, values, "fromCurrency", "toCurrency", "rate", "effectiveDate", "expiryDate"));
            case "lookupValues" -> lookupValueService.createLookupValue(bind(LookupValueDto::new, values, "category", "code", "value", "description", "sortOrder"));
            case "notificationChannels" -> notificationChannelService.createNotificationChannel(bind(NotificationChannelDto::new, values, "code", "name", "description"));
            case "notificationKeywords" -> notificationKeywordService.createNotificationKeyword(bind(NotificationKeywordDto::new, values, "code", "name", "exampleValue", "description"));
            case "notificationTemplates" -> notificationTemplateService.createNotificationTemplate(bind(NotificationTemplateDto::new, values, "code", "name", "channel", "subject", "body", "module"));
            case "reinsuranceCompanies" -> reinsuranceCompanyService.createReinsuranceCompany(bind(ReinsuranceCompanyDto::new, values, "code", "name", "country", "rating", "contactEmail", "contactPhone"));
            default -> throw new GlobalException("BLK-002", type);
        }
    }

    public String getSampleCsv(String type) {
        String[] cols = COLUMN_MAP.get(type);
        if (cols == null) return "Unknown type";
        return String.join(",", cols);
    }

    public Set<String> getSupportedTypes() {
        return COLUMN_MAP.keySet();
    }

    private <T> T bind(java.util.function.Supplier<T> supplier, Map<String, String> values, String... fields) {
        T dto = supplier.get();
        for (String field : fields) {
            String val = values.get(field);
            if (val == null || val.isEmpty()) continue;
            try {
                var prop = dto.getClass().getDeclaredField(field);
                var setter = dto.getClass().getMethod("set" + Character.toUpperCase(field.charAt(0)) + field.substring(1), prop.getType());
                Object converted = convert(prop.getType(), val);
                setter.invoke(dto, converted);
            } catch (NoSuchFieldException | NoSuchMethodException e) {
                // skip unknown fields
            } catch (Exception e) {
                throw new GlobalException("BLK-001", "Field '" + field + "': " + e.getCause().getMessage());
            }
        }
        return dto;
    }

    private Object convert(Class<?> type, String val) {
        if (type == String.class) return val;
        if (type == UUID.class) return UUID.fromString(val);
        if (type == Integer.class || type == int.class) return Integer.parseInt(val);
        if (type == Long.class || type == long.class) return Long.parseLong(val);
        if (type == Double.class || type == double.class) return Double.parseDouble(val);
        if (type == BigDecimal.class) return new BigDecimal(val);
        if (type == LocalDate.class) return LocalDate.parse(val, DateTimeFormatter.ISO_LOCAL_DATE);
        if (type == Boolean.class || type == boolean.class) return Boolean.parseBoolean(val);
        return val;
    }
}
