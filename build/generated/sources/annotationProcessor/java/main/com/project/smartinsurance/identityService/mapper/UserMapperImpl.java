package com.project.smartinsurance.identityService.mapper;

import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.commonService.dto.DocumentDto;
import com.project.smartinsurance.commonService.model.Document;
import com.project.smartinsurance.identityService.dto.UserDto;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.model.UserGroup;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-29T17:32:59+0545",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.3.0.jar, environment: Java 21.0.11 (Ubuntu)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setRoles( mapRoles( user.getRoles() ) );
        userDto.setStatus( mapStatus( user ) );
        userDto.setBranchId( userBranchId( user ) );
        userDto.setBranchCode( userBranchBranchCode( user ) );
        userDto.setBranchName( userBranchBranchName( user ) );
        userDto.setGroupId( userUserGroupId( user ) );
        userDto.setGroupName( userUserGroupName( user ) );
        userDto.setGroupCode( userUserGroupCode( user ) );
        userDto.setId( user.getId() );
        userDto.setUsername( user.getUsername() );
        userDto.setEmail( user.getEmail() );
        userDto.setFullName( user.getFullName() );
        userDto.setProfilePic( documentToDocumentDto( user.getProfilePic() ) );
        userDto.setLoginStartTime( user.getLoginStartTime() );
        userDto.setLoginEndTime( user.getLoginEndTime() );
        Set<Integer> set1 = user.getAllowedDaysOfWeek();
        if ( set1 != null ) {
            userDto.setAllowedDaysOfWeek( new LinkedHashSet<Integer>( set1 ) );
        }
        if ( user.getMfaEnabled() != null ) {
            userDto.setMfaEnabled( user.getMfaEnabled() );
        }

        return userDto;
    }

    @Override
    public User toEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.roles( mapCodesToRoles( userDto.getRoles() ) );
        user.username( userDto.getUsername() );
        user.email( userDto.getEmail() );
        user.fullName( userDto.getFullName() );
        user.profilePic( documentDtoToDocument( userDto.getProfilePic() ) );
        user.loginStartTime( userDto.getLoginStartTime() );
        user.loginEndTime( userDto.getLoginEndTime() );
        Set<Integer> set1 = userDto.getAllowedDaysOfWeek();
        if ( set1 != null ) {
            user.allowedDaysOfWeek( new LinkedHashSet<Integer>( set1 ) );
        }
        user.mfaEnabled( userDto.isMfaEnabled() );

        return user.build();
    }

    private UUID userBranchId(User user) {
        BranchEntity branch = user.getBranch();
        if ( branch == null ) {
            return null;
        }
        return branch.getId();
    }

    private String userBranchBranchCode(User user) {
        BranchEntity branch = user.getBranch();
        if ( branch == null ) {
            return null;
        }
        return branch.getBranchCode();
    }

    private String userBranchBranchName(User user) {
        BranchEntity branch = user.getBranch();
        if ( branch == null ) {
            return null;
        }
        return branch.getBranchName();
    }

    private UUID userUserGroupId(User user) {
        UserGroup userGroup = user.getUserGroup();
        if ( userGroup == null ) {
            return null;
        }
        return userGroup.getId();
    }

    private String userUserGroupName(User user) {
        UserGroup userGroup = user.getUserGroup();
        if ( userGroup == null ) {
            return null;
        }
        return userGroup.getName();
    }

    private String userUserGroupCode(User user) {
        UserGroup userGroup = user.getUserGroup();
        if ( userGroup == null ) {
            return null;
        }
        return userGroup.getCode();
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
}
