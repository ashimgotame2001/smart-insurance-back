package com.project.smartinsurance.customerService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Document;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.commonService.repository.DocumentRepository;
import com.project.smartinsurance.commonService.service.MinioService;
import com.project.smartinsurance.customerService.dto.*;
import com.project.smartinsurance.customerService.mapper.CustomerMapper;
import com.project.smartinsurance.customerService.model.*;
import com.project.smartinsurance.customerService.model.enums.CustomerType;
import com.project.smartinsurance.customerService.model.enums.KycStatus;
import com.project.smartinsurance.customerService.model.enums.OnboardingStatus;
import com.project.smartinsurance.customerService.repository.CustomerRepository;
import com.project.smartinsurance.customerService.repository.IdentityTypeConfigRepository;
import com.project.smartinsurance.customerService.repository.KYCRepository;
import com.project.smartinsurance.customerService.service.CustomerService;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final KYCRepository kycRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final IdentityTypeConfigRepository identityTypeConfigRepository;
    private final MinioService minioService;
    private final CustomerMapper mapper;

    private synchronized String generateCustomerCode() {
        long count = customerRepository.count();
        return "CUST-" + String.format("%06d", count + 1);
    }

    @Override
    @Transactional
    public IndividualCustomerDto createIndividualCustomer(IndividualCustomerDto dto) {
        IndividualCustomer customer = new IndividualCustomer();
        setBaseFields(customer, dto);
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setMiddleName(dto.getMiddleName());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setGender(dto.getGender() != null ? com.project.smartinsurance.customerService.model.enums.Gender.valueOf(dto.getGender()) : null);
        customer.setMaritalStatus(dto.getMaritalStatus());
        customer.setNationality(dto.getNationality());
        customer.setOccupation(dto.getOccupation());
        customer.setIdentityType(dto.getIdentityType());
        customer.setIdentityNumber(dto.getIdentityNumber());
        customer.setIdentityIssueDate(dto.getIdentityIssueDate());
        customer.setIdentityExpiryDate(dto.getIdentityExpiryDate());
        if (dto.getIdentityFrontDocId() != null) {
            customer.setIdentityFrontDoc(documentRepository.findById(dto.getIdentityFrontDocId()).orElse(null));
        }
        if (dto.getIdentityBackDocId() != null) {
            customer.setIdentityBackDoc(documentRepository.findById(dto.getIdentityBackDocId()).orElse(null));
        }
        customer.setFatherName(dto.getFatherName());
        customer.setMotherName(dto.getMotherName());
        customer.setPlaceOfBirth(dto.getPlaceOfBirth());
        return mapper.toIndividualDto(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public CorporateCustomerDto createCorporateCustomer(CorporateCustomerDto dto) {
        CorporateCustomer customer = new CorporateCustomer();
        setBaseFields(customer, dto);
        customer.setCompanyName(dto.getCompanyName());
        customer.setRegistrationNumber(dto.getRegistrationNumber());
        customer.setTaxId(dto.getTaxId());
        customer.setIncorporationDate(dto.getIncorporationDate());
        customer.setCompanyType(dto.getCompanyType());
        customer.setIndustry(dto.getIndustry());
        customer.setNumberOfEmployees(dto.getNumberOfEmployees());
        customer.setAuthorizedCapital(dto.getAuthorizedCapital());
        customer.setPaidUpCapital(dto.getPaidUpCapital());
        customer.setRegisteredAddress(dto.getRegisteredAddress());
        customer.setContactPersonName(dto.getContactPersonName());
        customer.setContactPersonDesignation(dto.getContactPersonDesignation());
        customer.setContactPersonPhone(dto.getContactPersonPhone());
        customer.setContactPersonEmail(dto.getContactPersonEmail());
        return mapper.toCorporateDto(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public GovernmentCustomerDto createGovernmentCustomer(GovernmentCustomerDto dto) {
        GovernmentCustomer customer = new GovernmentCustomer();
        setBaseFields(customer, dto);
        customer.setMinistryName(dto.getMinistryName());
        customer.setDepartmentName(dto.getDepartmentName());
        customer.setGovernmentLevel(dto.getGovernmentLevel() != null ? com.project.smartinsurance.customerService.model.enums.GovernmentLevel.valueOf(dto.getGovernmentLevel()) : null);
        customer.setEstablishedYear(dto.getEstablishedYear());
        customer.setBudgetCode(dto.getBudgetCode());
        customer.setFundingSource(dto.getFundingSource());
        customer.setDepartmentHeadName(dto.getDepartmentHeadName());
        customer.setDepartmentHeadDesignation(dto.getDepartmentHeadDesignation());
        customer.setDepartmentHeadPhone(dto.getDepartmentHeadPhone());
        customer.setDepartmentHeadEmail(dto.getDepartmentHeadEmail());
        customer.setOfficeAddress(dto.getOfficeAddress());
        return mapper.toGovernmentDto(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public IndividualCustomerDto updateIndividualCustomer(UUID id, IndividualCustomerDto dto) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new GlobalException("CUS-001"));
        if (!(existing instanceof IndividualCustomer customer)) throw new GlobalException("CUS-002");
        updateBaseFields(customer, dto);
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setMiddleName(dto.getMiddleName());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setGender(dto.getGender() != null ? com.project.smartinsurance.customerService.model.enums.Gender.valueOf(dto.getGender()) : null);
        customer.setMaritalStatus(dto.getMaritalStatus());
        customer.setNationality(dto.getNationality());
        customer.setOccupation(dto.getOccupation());
        customer.setIdentityType(dto.getIdentityType());
        customer.setIdentityNumber(dto.getIdentityNumber());
        customer.setIdentityIssueDate(dto.getIdentityIssueDate());
        customer.setIdentityExpiryDate(dto.getIdentityExpiryDate());
        if (dto.getIdentityFrontDocId() != null) {
            customer.setIdentityFrontDoc(documentRepository.findById(dto.getIdentityFrontDocId()).orElse(null));
        } else {
            customer.setIdentityFrontDoc(null);
        }
        if (dto.getIdentityBackDocId() != null) {
            customer.setIdentityBackDoc(documentRepository.findById(dto.getIdentityBackDocId()).orElse(null));
        } else {
            customer.setIdentityBackDoc(null);
        }
        customer.setFatherName(dto.getFatherName());
        customer.setMotherName(dto.getMotherName());
        customer.setPlaceOfBirth(dto.getPlaceOfBirth());
        return mapper.toIndividualDto(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public CorporateCustomerDto updateCorporateCustomer(UUID id, CorporateCustomerDto dto) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new GlobalException("CUS-001"));
        if (!(existing instanceof CorporateCustomer customer)) throw new GlobalException("CUS-002");
        updateBaseFields(customer, dto);
        customer.setCompanyName(dto.getCompanyName());
        customer.setRegistrationNumber(dto.getRegistrationNumber());
        customer.setTaxId(dto.getTaxId());
        customer.setIncorporationDate(dto.getIncorporationDate());
        customer.setCompanyType(dto.getCompanyType());
        customer.setIndustry(dto.getIndustry());
        customer.setNumberOfEmployees(dto.getNumberOfEmployees());
        customer.setAuthorizedCapital(dto.getAuthorizedCapital());
        customer.setPaidUpCapital(dto.getPaidUpCapital());
        customer.setRegisteredAddress(dto.getRegisteredAddress());
        customer.setContactPersonName(dto.getContactPersonName());
        customer.setContactPersonDesignation(dto.getContactPersonDesignation());
        customer.setContactPersonPhone(dto.getContactPersonPhone());
        customer.setContactPersonEmail(dto.getContactPersonEmail());
        return mapper.toCorporateDto(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public GovernmentCustomerDto updateGovernmentCustomer(UUID id, GovernmentCustomerDto dto) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new GlobalException("CUS-001"));
        if (!(existing instanceof GovernmentCustomer customer)) throw new GlobalException("CUS-002");
        updateBaseFields(customer, dto);
        customer.setMinistryName(dto.getMinistryName());
        customer.setDepartmentName(dto.getDepartmentName());
        customer.setGovernmentLevel(dto.getGovernmentLevel() != null ? com.project.smartinsurance.customerService.model.enums.GovernmentLevel.valueOf(dto.getGovernmentLevel()) : null);
        customer.setEstablishedYear(dto.getEstablishedYear());
        customer.setBudgetCode(dto.getBudgetCode());
        customer.setFundingSource(dto.getFundingSource());
        customer.setDepartmentHeadName(dto.getDepartmentHeadName());
        customer.setDepartmentHeadDesignation(dto.getDepartmentHeadDesignation());
        customer.setDepartmentHeadPhone(dto.getDepartmentHeadPhone());
        customer.setDepartmentHeadEmail(dto.getDepartmentHeadEmail());
        customer.setOfficeAddress(dto.getOfficeAddress());
        return mapper.toGovernmentDto(customerRepository.save(customer));
    }

    @Override
    public CustomerDto getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new GlobalException("CUS-001"));
        return mapper.toDto(customer);
    }

    @Override
    public PagedData<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        return getAllCustomers(page, size, sortBy, sortDir, null);
    }

    @Override
    public PagedData<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir, String customerType) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Customer> result = customerType != null && !customerType.isBlank()
                ? customerRepository.findByCustomerType(CustomerType.valueOf(customerType.toUpperCase()), pageRequest)
                : customerRepository.findAll(pageRequest);
        List<CustomerDto> content = result.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
        return PagedData.<CustomerDto>builder()
                .content(content).page(result.getNumber()).size(result.getSize())
                .totalElements(result.getTotalElements()).totalPages(result.getTotalPages()).build();
    }

    @Override
    public List<CustomerDto> searchCustomers(String query) {
        List<Customer> all = customerRepository.findAll();
        return all.stream()
                .filter(c -> c.getCustomerCode().toLowerCase().contains(query.toLowerCase())
                        || (c.getEmail() != null && c.getEmail().toLowerCase().contains(query.toLowerCase()))
                        || (c.getPhone() != null && c.getPhone().contains(query)))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagedData<CustomerDto> searchCustomers(CustomerSearchRequest request) {
        List<Customer> all = customerRepository.findAll();
        Stream<Customer> stream = all.stream();

        if (request.getCustomerCode() != null && !request.getCustomerCode().isBlank()) {
            stream = stream.filter(c -> c.getCustomerCode().toLowerCase().contains(request.getCustomerCode().toLowerCase()));
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            stream = stream.filter(c -> c.getEmail() != null && c.getEmail().toLowerCase().contains(request.getEmail().toLowerCase()));
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            stream = stream.filter(c -> c.getPhone() != null && c.getPhone().contains(request.getPhone()));
        }
        if (request.getCustomerType() != null && !request.getCustomerType().isBlank()) {
            CustomerType type = CustomerType.valueOf(request.getCustomerType().toUpperCase());
            stream = stream.filter(c -> c.getCustomerType() == type);
        }

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            stream = stream.filter(c -> c instanceof IndividualCustomer ic
                    && ic.getFirstName() != null && ic.getFirstName().toLowerCase().contains(request.getFirstName().toLowerCase()));
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            stream = stream.filter(c -> c instanceof IndividualCustomer ic
                    && ic.getLastName() != null && ic.getLastName().toLowerCase().contains(request.getLastName().toLowerCase()));
        }
        if (request.getIdentityNumber() != null && !request.getIdentityNumber().isBlank()) {
            stream = stream.filter(c -> c instanceof IndividualCustomer ic
                    && ic.getIdentityNumber() != null && ic.getIdentityNumber().toLowerCase().contains(request.getIdentityNumber().toLowerCase()));
        }
        if (request.getCompanyName() != null && !request.getCompanyName().isBlank()) {
            stream = stream.filter(c -> c instanceof CorporateCustomer cc
                    && cc.getCompanyName() != null && cc.getCompanyName().toLowerCase().contains(request.getCompanyName().toLowerCase()));
        }
        if (request.getDepartmentName() != null && !request.getDepartmentName().isBlank()) {
            stream = stream.filter(c -> c instanceof GovernmentCustomer gc
                    && gc.getDepartmentName() != null && gc.getDepartmentName().toLowerCase().contains(request.getDepartmentName().toLowerCase()));
        }

        List<CustomerDto> filtered = stream.map(mapper::toDto).collect(Collectors.toList());
        int page = request.getPage();
        int size = request.getSize() > 0 ? request.getSize() : 10;
        int totalElements = filtered.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);
        List<CustomerDto> pageContent = fromIndex < totalElements ? filtered.subList(fromIndex, toIndex) : List.of();

        return PagedData.<CustomerDto>builder()
                .content(pageContent).page(page).size(size)
                .totalElements(totalElements).totalPages(totalPages).build();
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new GlobalException("CUS-001"));
        customer.setStatus(Status.DELETED);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public KYCDto addKycDocument(UUID customerId, KYCDto dto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new GlobalException("CUS-001"));

        IdentityTypeConfig identityTypeConfig = null;
        if (dto.getDocumentType() != null) {
            identityTypeConfig = identityTypeConfigRepository.findByCode(dto.getDocumentType())
                    .orElseThrow(() -> new GlobalException("ITC-001", dto.getDocumentType()));
        }

        KYC kyc = new KYC();
        kyc.setCustomer(customer);
        kyc.setDocumentType(identityTypeConfig);
        kyc.setDocumentNumber(dto.getDocumentNumber());
        if (dto.getFrontDocumentId() != null) {
            kyc.setFrontDocument(documentRepository.findById(dto.getFrontDocumentId()).orElse(null));
        } else if (identityTypeConfig != null && identityTypeConfig.isRequiresFrontImage()) {
            throw new GlobalException("KYC-002", "Front image is required for " + identityTypeConfig.getName());
        }
        if (dto.getBackDocumentId() != null) {
            kyc.setBackDocument(documentRepository.findById(dto.getBackDocumentId()).orElse(null));
        } else if (identityTypeConfig != null && identityTypeConfig.isRequiresBackImage()) {
            throw new GlobalException("KYC-003", "Back image is required for " + identityTypeConfig.getName());
        }
        kyc.setExpiryDate(dto.getExpiryDate());
        kyc.setIssueDate(dto.getIssueDate());
        kyc.setVerificationStatus("PENDING");
        kyc.setRemarks(dto.getRemarks());
        kyc = kycRepository.save(kyc);

        customer.setKycStatus(KycStatus.PENDING);
        customer.setOnboardingStatus(OnboardingStatus.KYC_PENDING);
        customerRepository.save(customer);

        refreshDocumentUrls(kyc);

        return mapper.toKycDto(kyc);
    }

    @Override
    public List<KYCDto> getCustomerKyc(UUID customerId) {
        return kycRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .peek(this::refreshDocumentUrls)
                .map(mapper::toKycDto).collect(Collectors.toList());
    }

    private void refreshDocumentUrls(KYC kyc) {
        if (kyc.getFrontDocument() != null) {
            kyc.getFrontDocument().setUrl(minioService.getFileUrl(kyc.getFrontDocument().getFileName()));
        }
        if (kyc.getBackDocument() != null) {
            kyc.getBackDocument().setUrl(minioService.getFileUrl(kyc.getBackDocument().getFileName()));
        }
    }

    @Override
    @Transactional
    public KYCDto verifyKyc(UUID kycId, KYCDto dto) {
        KYC kyc = kycRepository.findById(kycId).orElseThrow(() -> new GlobalException("KYC-001"));
        kyc.setVerificationStatus(dto.getVerificationStatus());
        kyc.setRejectionReason(dto.getRejectionReason());
        kyc.setRemarks(dto.getRemarks());

        if (dto.getVerifiedById() != null) {
            User verifier = userRepository.findById(dto.getVerifiedById()).orElse(null);
            kyc.setVerifiedBy(verifier);
        }
        kyc.setVerifiedAt(LocalDateTime.now());
        kyc = kycRepository.save(kyc);

        Customer customer = kyc.getCustomer();
        if ("VERIFIED".equals(dto.getVerificationStatus())) {
            boolean allVerified = kycRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId())
                    .stream().allMatch(k -> "VERIFIED".equals(k.getVerificationStatus()));
            if (allVerified) {
                customer.setKycStatus(KycStatus.VERIFIED);
                customer.setOnboardingStatus(OnboardingStatus.COMPLETED);
            }
        } else if ("REJECTED".equals(dto.getVerificationStatus())) {
            customer.setKycStatus(KycStatus.REJECTED);
        }
        customerRepository.save(customer);

        refreshDocumentUrls(kyc);

        return mapper.toKycDto(kyc);
    }

    private void setBaseFields(Customer customer, CustomerDto dto) {
        customer.setCustomerCode(generateCustomerCode());
        customer.setStatus(Status.ACTIVE);
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setPrimaryAddress(dto.getPrimaryAddress());
        customer.setKycStatus(KycStatus.PENDING);
        customer.setOnboardingStatus(OnboardingStatus.INITIATED);
        customer.setNotes(dto.getNotes());
    }

    private void updateBaseFields(Customer customer, CustomerDto dto) {
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setPrimaryAddress(dto.getPrimaryAddress());
        customer.setNotes(dto.getNotes());
    }
}
