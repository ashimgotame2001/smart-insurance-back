package com.project.smartinsurance.identityService.mapper;

import com.project.smartinsurance.identityService.dto.UserDto;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    @Mapping(target = "status", source = "user", qualifiedByName = "mapStatus")
    @Mapping(target = "branchId", source = "user.branch.id")
    @Mapping(target = "branchCode", source = "user.branch.branchCode")
    @Mapping(target = "branchName", source = "user.branch.branchName")
    @Mapping(target = "groupId", source = "user.userGroup.id")
    @Mapping(target = "groupName", source = "user.userGroup.name")
    @Mapping(target = "groupCode", source = "user.userGroup.code")
    UserDto toDto(User user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapCodesToRoles")
    User toEntity(UserDto userDto);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream().map(Role::getCode).collect(Collectors.toSet());
    }

    @Named("mapCodesToRoles")
    default Set<Role> mapCodesToRoles(Set<String> codes) {
        if (codes == null) return null;
        return codes.stream()
                .map(code -> Role.builder().code(code).build())
                .collect(Collectors.toSet());
    }

    @Named("mapStatus")
    default String mapStatus(User user) {
        if (user == null) return "Inactive";
        return user.isEnabled() && user.isAccountNonLocked() ? "Active" : "Locked";
    }
}
