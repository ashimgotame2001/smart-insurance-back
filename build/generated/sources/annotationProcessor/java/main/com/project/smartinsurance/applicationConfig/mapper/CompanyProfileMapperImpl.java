package com.project.smartinsurance.applicationConfig.mapper;

import com.project.smartinsurance.applicationConfig.dto.BranchDto;
import com.project.smartinsurance.applicationConfig.dto.CompanyProfileDto;
import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.applicationConfig.model.CompanyProfileEntity;
import com.project.smartinsurance.commonService.dto.DocumentDto;
import com.project.smartinsurance.commonService.model.Document;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-29T10:51:39+0545",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CompanyProfileMapperImpl implements CompanyProfileMapper {

    @Override
    public CompanyProfileDto toDto(CompanyProfileEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CompanyProfileDto.CompanyProfileDtoBuilder companyProfileDto = CompanyProfileDto.builder();

        companyProfileDto.id( entity.getId() );
        companyProfileDto.status( entity.getStatus() );
        companyProfileDto.companyCode( entity.getCompanyCode() );
        companyProfileDto.companyName( entity.getCompanyName() );
        companyProfileDto.companyShortName( entity.getCompanyShortName() );
        companyProfileDto.registrationNumber( entity.getRegistrationNumber() );
        companyProfileDto.taxIdentificationNumber( entity.getTaxIdentificationNumber() );
        companyProfileDto.licenseNumber( entity.getLicenseNumber() );
        companyProfileDto.websiteUrl( entity.getWebsiteUrl() );
        companyProfileDto.email( entity.getEmail() );
        companyProfileDto.phoneNumber( entity.getPhoneNumber() );
        companyProfileDto.supportEmail( entity.getSupportEmail() );
        companyProfileDto.supportPhone( entity.getSupportPhone() );
        companyProfileDto.addressLine1( entity.getAddressLine1() );
        companyProfileDto.addressLine2( entity.getAddressLine2() );
        companyProfileDto.city( entity.getCity() );
        companyProfileDto.state( entity.getState() );
        companyProfileDto.country( entity.getCountry() );
        companyProfileDto.postalCode( entity.getPostalCode() );
        companyProfileDto.logoUrl( entity.getLogoUrl() );
        companyProfileDto.logo( documentToDocumentDto( entity.getLogo() ) );
        companyProfileDto.faviconUrl( entity.getFaviconUrl() );
        companyProfileDto.favicon( documentToDocumentDto( entity.getFavicon() ) );
        companyProfileDto.primaryColor( entity.getPrimaryColor() );
        companyProfileDto.secondaryColor( entity.getSecondaryColor() );
        companyProfileDto.currencyCode( entity.getCurrencyCode() );
        companyProfileDto.currencyFormat( entity.getCurrencyFormat() );
        companyProfileDto.timezone( entity.getTimezone() );
        companyProfileDto.businessType( entity.getBusinessType() );
        companyProfileDto.companyStatus( entity.getCompanyStatus() );
        companyProfileDto.establishedDate( entity.getEstablishedDate() );
        companyProfileDto.remarks( entity.getRemarks() );

        return companyProfileDto.build();
    }

    @Override
    public CompanyProfileEntity toEntity(CompanyProfileDto dto) {
        if ( dto == null ) {
            return null;
        }

        CompanyProfileEntity.CompanyProfileEntityBuilder companyProfileEntity = CompanyProfileEntity.builder();

        companyProfileEntity.companyCode( dto.getCompanyCode() );
        companyProfileEntity.companyName( dto.getCompanyName() );
        companyProfileEntity.companyShortName( dto.getCompanyShortName() );
        companyProfileEntity.registrationNumber( dto.getRegistrationNumber() );
        companyProfileEntity.taxIdentificationNumber( dto.getTaxIdentificationNumber() );
        companyProfileEntity.licenseNumber( dto.getLicenseNumber() );
        companyProfileEntity.websiteUrl( dto.getWebsiteUrl() );
        companyProfileEntity.email( dto.getEmail() );
        companyProfileEntity.phoneNumber( dto.getPhoneNumber() );
        companyProfileEntity.supportEmail( dto.getSupportEmail() );
        companyProfileEntity.supportPhone( dto.getSupportPhone() );
        companyProfileEntity.addressLine1( dto.getAddressLine1() );
        companyProfileEntity.addressLine2( dto.getAddressLine2() );
        companyProfileEntity.city( dto.getCity() );
        companyProfileEntity.state( dto.getState() );
        companyProfileEntity.country( dto.getCountry() );
        companyProfileEntity.postalCode( dto.getPostalCode() );
        companyProfileEntity.logoUrl( dto.getLogoUrl() );
        companyProfileEntity.logo( documentDtoToDocument( dto.getLogo() ) );
        companyProfileEntity.faviconUrl( dto.getFaviconUrl() );
        companyProfileEntity.favicon( documentDtoToDocument( dto.getFavicon() ) );
        companyProfileEntity.primaryColor( dto.getPrimaryColor() );
        companyProfileEntity.secondaryColor( dto.getSecondaryColor() );
        companyProfileEntity.currencyCode( dto.getCurrencyCode() );
        companyProfileEntity.currencyFormat( dto.getCurrencyFormat() );
        companyProfileEntity.timezone( dto.getTimezone() );
        companyProfileEntity.businessType( dto.getBusinessType() );
        companyProfileEntity.companyStatus( dto.getCompanyStatus() );
        companyProfileEntity.establishedDate( dto.getEstablishedDate() );
        companyProfileEntity.remarks( dto.getRemarks() );

        return companyProfileEntity.build();
    }

    @Override
    public BranchDto toDto(BranchEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BranchDto.BranchDtoBuilder branchDto = BranchDto.builder();

        branchDto.parentBranchId( entityParentBranchId( entity ) );
        branchDto.id( entity.getId() );
        branchDto.status( entity.getStatus() );
        branchDto.branchCode( entity.getBranchCode() );
        branchDto.branchName( entity.getBranchName() );
        branchDto.branchType( entity.getBranchType() );
        branchDto.branchStatus( entity.getBranchStatus() );
        branchDto.email( entity.getEmail() );
        branchDto.phoneNumber( entity.getPhoneNumber() );
        branchDto.addressLine1( entity.getAddressLine1() );
        branchDto.addressLine2( entity.getAddressLine2() );
        branchDto.city( entity.getCity() );
        branchDto.state( entity.getState() );
        branchDto.country( entity.getCountry() );
        branchDto.postalCode( entity.getPostalCode() );

        return branchDto.build();
    }

    @Override
    public BranchEntity toEntity(BranchDto dto) {
        if ( dto == null ) {
            return null;
        }

        BranchEntity.BranchEntityBuilder branchEntity = BranchEntity.builder();

        branchEntity.parentBranch( mapBranchIdToBranch( dto.getParentBranchId() ) );
        branchEntity.branchCode( dto.getBranchCode() );
        branchEntity.branchName( dto.getBranchName() );
        branchEntity.branchType( dto.getBranchType() );
        branchEntity.branchStatus( dto.getBranchStatus() );
        branchEntity.email( dto.getEmail() );
        branchEntity.phoneNumber( dto.getPhoneNumber() );
        branchEntity.addressLine1( dto.getAddressLine1() );
        branchEntity.addressLine2( dto.getAddressLine2() );
        branchEntity.city( dto.getCity() );
        branchEntity.state( dto.getState() );
        branchEntity.country( dto.getCountry() );
        branchEntity.postalCode( dto.getPostalCode() );

        return branchEntity.build();
    }

    protected DocumentDto documentToDocumentDto(Document document) {
        if ( document == null ) {
            return null;
        }

        DocumentDto.DocumentDtoBuilder documentDto = DocumentDto.builder();

        documentDto.id( document.getId() );
        documentDto.fileName( document.getFileName() );
        documentDto.originalFileName( document.getOriginalFileName() );
        documentDto.fileType( document.getFileType() );
        documentDto.fileSize( document.getFileSize() );
        documentDto.url( document.getUrl() );

        return documentDto.build();
    }

    protected Document documentDtoToDocument(DocumentDto documentDto) {
        if ( documentDto == null ) {
            return null;
        }

        Document.DocumentBuilder document = Document.builder();

        document.fileName( documentDto.getFileName() );
        document.originalFileName( documentDto.getOriginalFileName() );
        document.fileType( documentDto.getFileType() );
        document.fileSize( documentDto.getFileSize() );
        document.url( documentDto.getUrl() );

        return document.build();
    }

    private UUID entityParentBranchId(BranchEntity branchEntity) {
        BranchEntity parentBranch = branchEntity.getParentBranch();
        if ( parentBranch == null ) {
            return null;
        }
        return parentBranch.getId();
    }
}
