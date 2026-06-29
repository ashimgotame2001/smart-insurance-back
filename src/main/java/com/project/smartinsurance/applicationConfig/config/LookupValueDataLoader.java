package com.project.smartinsurance.applicationConfig.config;

import com.project.smartinsurance.applicationConfig.model.LookupValue;
import com.project.smartinsurance.applicationConfig.repository.LookupValueRepository;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class LookupValueDataLoader implements CommandLineRunner {

    private final LookupValueRepository lookupValueRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (lookupValueRepository.findByCategory("IDENTITY_TYPE").isEmpty()) {
            List<LookupValue> defaults = List.of(
                    createLookupValue("IDENTITY_TYPE", "NATIONAL_ID", "National ID", "Government-issued national identification card", 1),
                    createLookupValue("IDENTITY_TYPE", "PASSPORT", "Passport", "International travel passport", 2),
                    createLookupValue("IDENTITY_TYPE", "DRIVING_LICENSE", "Driving License", "Government-issued driving license", 3),
                    createLookupValue("IDENTITY_TYPE", "VOTER_ID", "Voter ID", "Voter identification card", 4)
            );
            lookupValueRepository.saveAll(defaults);
        }
        if (lookupValueRepository.findByCategory("GENDER").isEmpty()) {
            List<LookupValue> defaults = List.of(
                    createLookupValue("GENDER", "MALE", "Male", "Male", 1),
                    createLookupValue("GENDER", "FEMALE", "Female", "Female", 2),
                    createLookupValue("GENDER", "OTHER", "Other", "Other", 3)
            );
            lookupValueRepository.saveAll(defaults);
        }
        if (lookupValueRepository.findByCategory("MARITAL_STATUS").isEmpty()) {
            List<LookupValue> defaults = List.of(
                    createLookupValue("MARITAL_STATUS", "SINGLE", "Single", "Single", 1),
                    createLookupValue("MARITAL_STATUS", "MARRIED", "Married", "Married", 2),
                    createLookupValue("MARITAL_STATUS", "DIVORCED", "Divorced", "Divorced", 3),
                    createLookupValue("MARITAL_STATUS", "WIDOWED", "Widowed", "Widowed", 4)
            );
            lookupValueRepository.saveAll(defaults);
        }
        if (lookupValueRepository.findByCategory("COMPANY_TYPE").isEmpty()) {
            List<LookupValue> defaults = List.of(
                    createLookupValue("COMPANY_TYPE", "PRIVATE_LIMITED", "Private Limited", "Private Limited Company", 1),
                    createLookupValue("COMPANY_TYPE", "PUBLIC_LIMITED", "Public Limited", "Public Limited Company", 2),
                    createLookupValue("COMPANY_TYPE", "PARTNERSHIP", "Partnership", "Partnership", 3),
                    createLookupValue("COMPANY_TYPE", "SOLE_PROPRIETORSHIP", "Sole Proprietorship", "Sole Proprietorship", 4),
                    createLookupValue("COMPANY_TYPE", "LLC", "LLC", "Limited Liability Company", 5),
                    createLookupValue("COMPANY_TYPE", "NON_PROFIT", "Non-Profit", "Non-Profit Organization", 6)
            );
            lookupValueRepository.saveAll(defaults);
        }
    }

    private LookupValue createLookupValue(String category, String code, String value, String description, int sortOrder) {
        LookupValue lv = new LookupValue();
        lv.setCategory(category);
        lv.setCode(code);
        lv.setValue(value);
        lv.setDescription(description);
        lv.setSortOrder(sortOrder);
        lv.setStatus(Status.ACTIVE);
        return lv;
    }
}
