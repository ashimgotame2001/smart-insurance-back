# Service Template

## Interface (`application/CustomerService.java`)

See [service-interface-template.md](service-interface-template.md).

## Implementation (`application/impl/CustomerServiceImpl.java`)

Service impl is an **orchestrator only** — delegate to validation and mapping layers.

```java
package com.swifttech.edx.ipr.eam.service.impl;

import com.swifttech.edx.dm.am.entity.ApplicationModuleEntity;
import com.swifttech.edx.dm.am.entity.ApplicationResourceEntity;
import com.swifttech.edx.dm.am.entity.ApplicationServiceEntity;
import com.swifttech.edx.dm.am.enums.ErrorCodeEnum;
import com.swifttech.edx.dm.am.enums.SuccessCodeEnum;
import com.swifttech.edx.dm.am.repository.ApplicationResourceRepository;
import com.swifttech.edx.dm.enums.StatusEnum;
import com.swifttech.edx.dm.exception.GlobalException;
import com.swifttech.edx.dm.payload.request.StatusUpdateRequest;
import com.swifttech.edx.dm.payload.response.DataPaginationResponse;
import com.swifttech.edx.dm.payload.response.GlobalResponse;
import com.swifttech.edx.dm.util.Helper;
import com.swifttech.edx.dm.util.MessageHelper;
import com.swifttech.edx.ipr.eam.entity.RoleEntity;
import com.swifttech.edx.ipr.eam.model.Role;
import com.swifttech.edx.ipr.eam.model.request.BulkRoleUpdateRequest;
import com.swifttech.edx.ipr.eam.model.request.PermissionRequest;
import com.swifttech.edx.ipr.eam.model.request.RoleDataRequest;
import com.swifttech.edx.ipr.eam.model.response.ApplicationModuleResponse;
import com.swifttech.edx.ipr.eam.model.response.ApplicationOperationResponse;
import com.swifttech.edx.ipr.eam.model.response.ApplicationServiceResponse;
import com.swifttech.edx.ipr.eam.model.response.CustomRoleResponse;
import com.swifttech.edx.ipr.eam.model.response.RoleResponse;
import com.swifttech.edx.ipr.eam.repository.RoleRepository;
import com.swifttech.edx.ipr.eam.service.RoleService;
import com.swifttech.edx.ipr.eam.service.specification.RoleSpecification;
import com.swifttech.edx.ipr.eam.service.validation.PermissionValidationService;
import com.swifttech.edx.ipr.eam.service.validation.RoleValidationService;
import com.swifttech.edx.ipr.eam.utils.CommonDatabaseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final RoleValidationService roleValidationService;
    private final PermissionValidationService permissionValidationService;
    private final ApplicationResourceRepository applicationResourceRepository;
    private final ModelMapper modelMapper;
    private final CommonDatabaseUtils commonDatabaseUtils;


    @Override
    public GlobalResponse createRole(Role request) throws GlobalException {
        roleValidationService.validateRoleCreation(request);
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(request.getName());
        roleEntity.setDescription(request.getDescription());
        roleEntity.setStatus(StatusEnum.ACTIVE);
        List<ApplicationResourceEntity> permissionEntities = permissionValidationService.checkAndMapPermission(request.getPermissions());
        roleEntity.setPermissions(permissionEntities);
        handlePersist(roleEntity);
        return MessageHelper.buildSuccessResponse(SuccessCodeEnum._100.getMessage());
    }


    @Override
    public GlobalResponse updateRole(Role request, Long id) throws GlobalException {
        RoleEntity roleEntity = commonDatabaseUtils.getRoleById(id);
        roleValidationService.validateRoleUpdate(request, roleEntity);
        roleEntity.setName(request.getName());
        roleEntity.setDescription(request.getDescription());
        roleEntity.setStatus(StatusEnum.ACTIVE);
        List<ApplicationResourceEntity> permissionEntities = permissionValidationService.validateAndMapPermissions(request.getPermissions(), roleEntity.getPermissions());
        roleEntity.setPermissions(permissionEntities);
        handlePersist(roleEntity);
        return MessageHelper.buildSuccessResponse(SuccessCodeEnum._100.getMessage());
    }

    @Override
    public GlobalResponse updateRolesInBulk(List<BulkRoleUpdateRequest> requests) throws GlobalException {
        // Fetch all roles in a single query
        List<Long> roleIds = requests.stream().map(BulkRoleUpdateRequest::getUid).toList();
        Map<Long, RoleEntity> roleEntityMap = repository.findAllById(roleIds).stream()
                .collect(Collectors.toMap(RoleEntity::getUid, Function.identity()));

        // Validate all requested role IDs were found
        if (roleEntityMap.size() != roleIds.size()) {
            throw new GlobalException(ErrorCodeEnum._001.getMessage());
        }

        // Validate all roles are active
        for (RoleEntity role : roleEntityMap.values()) {
            if (!StatusEnum.ACTIVE.equals(role.getStatus())) {
                throw new GlobalException(ErrorCodeEnum._006.getMessage());
            }
        }

        // Fetch all permissions in a single query
        Set<Long> allPermissionIds = requests.stream()
                .flatMap(r -> r.getPermissions().stream().map(PermissionRequest::getUid))
                .collect(Collectors.toSet());

        Map<Long, ApplicationResourceEntity> resourceMap = applicationResourceRepository.findAllById(allPermissionIds).stream()
                .collect(Collectors.toMap(ApplicationResourceEntity::getUid, Function.identity()));

        // Validate all permissions exist and are active
        for (Long permId : allPermissionIds) {
            ApplicationResourceEntity resource = resourceMap.get(permId);
            if (resource == null) throw new GlobalException(ErrorCodeEnum._001.getMessage());
            if (!StatusEnum.ACTIVE.equals(resource.getStatus()))
                throw new GlobalException(ErrorCodeEnum._006.getMessage());
        }

        // Apply updates in memory
        for (BulkRoleUpdateRequest item : requests) {
            RoleEntity roleEntity = roleEntityMap.get(item.getUid());
            List<ApplicationResourceEntity> permissionEntities = item.getPermissions() == null
                    ? Collections.emptyList()
                    : item.getPermissions().stream()
                    .map(p -> resourceMap.get(p.getUid()))
                    .collect(Collectors.toCollection(ArrayList::new));
            roleEntity.setPermissions(permissionEntities);
        }

        // Persist all in a single saveAll call
        repository.saveAll(roleEntityMap.values());
        return MessageHelper.buildSuccessResponse(SuccessCodeEnum._100.getMessage());
    }

    @Override
    public GlobalResponse updateRoleStatus(StatusUpdateRequest request, Long id) throws GlobalException {
        RoleEntity roleEntity = commonDatabaseUtils.getRoleById(id);
        roleEntity.setStatus(request.getStatus());
        handlePersist(roleEntity);
        return MessageHelper.buildSuccessResponse(SuccessCodeEnum._100.getMessage());
    }

    @Override
    public GlobalResponse findRoleById(Long id) throws GlobalException {
        RoleEntity roleEntity = commonDatabaseUtils.getRoleById(id);
        RoleResponse roleResponse = mapToResponse(roleEntity);
        return MessageHelper.buildSuccessResponseWithData(SuccessCodeEnum._100.getMessage(), roleResponse);
    }

    @Override
    public GlobalResponse findRolePaginatedData(RoleDataRequest request) throws GlobalException {
        Pageable page = Helper.getPageable(request);
        Specification<RoleEntity> spec = RoleSpecification.getAllByFilter(request);
        Page<RoleEntity> roleEntities = repository.findAll(spec, page);
        DataPaginationResponse response = DataPaginationResponse.builder()
                .result(roleEntities.getContent().stream().map(e -> modelMapper.map(e, CustomRoleResponse.class)).toList())
                .totalElementCount(roleEntities.getTotalElements())
                .build();

        return MessageHelper.buildSuccessResponseWithData(SuccessCodeEnum._100.getMessage(), response);
    }

    @Override
    public GlobalResponse findAllRole() throws GlobalException {
        return MessageHelper.buildSuccessResponse(SuccessCodeEnum._100.getMessage());
    }

    private void handlePersist(RoleEntity roleEntity) throws GlobalException {
        this.repository.save(roleEntity);
    }

    public static RoleResponse mapToResponse(RoleEntity role) {

        Map<ApplicationModuleEntity, List<ApplicationResourceEntity>> moduleGrouped =
                role.getPermissions().stream()
                        .collect(Collectors.groupingBy(ApplicationResourceEntity::getApplicationModule));

        List<ApplicationModuleResponse> moduleResponses = new ArrayList<>();

        for (Map.Entry<ApplicationModuleEntity, List<ApplicationResourceEntity>> moduleEntry : moduleGrouped.entrySet()) {
            ApplicationModuleEntity module = moduleEntry.getKey();
            List<ApplicationResourceEntity> moduleResources = moduleEntry.getValue();

            Map<ApplicationServiceEntity, List<ApplicationResourceEntity>> serviceGrouped =
                    moduleResources.stream()
                            .collect(Collectors.groupingBy(ApplicationResourceEntity::getApplicationService));

            List<ApplicationServiceResponse> serviceResponses = new ArrayList<>();

            for (Map.Entry<ApplicationServiceEntity, List<ApplicationResourceEntity>> serviceEntry : serviceGrouped.entrySet()) {

                ApplicationServiceEntity service = serviceEntry.getKey();
                List<ApplicationResourceEntity> serviceResources = serviceEntry.getValue();

                List<ApplicationOperationResponse> operationResponses =
                        serviceResources.stream()
                                .map(resource -> new ApplicationOperationResponse(
                                        resource.getUid(),
                                        resource.getName(),
                                        resource.getDescription(),
                                        resource.getEndPoint()
                                ))
                                .collect(Collectors.toList());

                // Build service response WITH operations
                ApplicationServiceResponse serviceResponse = new ApplicationServiceResponse(
                        service.getUid(),
                        service.getName(),
                        service.getDescription(),
                        service.getStatus(),
                        operationResponses
                );

                serviceResponses.add(serviceResponse);
            }

            // Build module response
            ApplicationModuleResponse moduleResponse = new ApplicationModuleResponse(
                    module.getUid(),
                    module.getName(),
                    module.getDescription(),
                    module.getStatus(),
                    serviceResponses
            );

            moduleResponses.add(moduleResponse);
        }

        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .permissions(moduleResponses)
                .build();
    }


    private ApplicationResourceEntity getResourceEntity(
            PermissionRequest permission,
            ApplicationResourceRepository repository
    ) throws GlobalException {
        ApplicationResourceEntity resource = commonDatabaseUtils.getResourceById(permission.getUid());
        if (!resource.getStatus().equals(StatusEnum.ACTIVE)) throw new GlobalException(ErrorCodeEnum._006.getMessage());
        return resource;
    }


}
```

## Orchestration Order (commands)

| Step | Layer | Call |
|------|-------|------|
| 1 | Validation | `xxxValidationService.createValidate(request)` |
| 2 | Direct mapping (MapStruct) | `customerMapper.toEntity(request)` |
| 3 | Persistence | `repository.save(entity)` |
| 4 | Events (optional) | `eventPublisher.publish(...)` after save |
| 5 | Direct mapping (MapStruct) | `customerMapper.toResponse(saved)` |
| 6 | Response | `MessageHelper.buildSuccessResponseWithData(...)` |

## Infrastructure port (optional — `service/StorageService.java`)

```java
public interface StorageService {
    String uploadFile(byte[] data, String contentType, String objectPath) throws Exception;
    InputStream retrieveFile(String objectPath) throws Exception;
}
```

Implementation in `service/impl/` (e.g. `AwsS3StorageService`). Used internally by application service — **not** returned as `GlobalResponse` from port methods.

## Rules

- **`XxxService` interface** + **`XxxServiceImpl`** in `application/impl/`
- **Inject** `XxxValidationService` (always) and **MapStruct `XxxMapper`** (direct, default)
- Optional **`XxxMappingService`** only for custom/enriched mapping — see [mapping-service-template.md](mapping-service-template.md)
- Command methods follow **validate → map → save → map → respond**
- Return **`GlobalResponse<T>`** from application service methods exposed to REST
- Build success via **`MessageHelper.buildSuccessResponseWithData(code, data)`**
- Throw **`GlobalException`** from validation layer or for not-found — see [../programming/exception-handling-skill.md](../programming/exception-handling-skill.md)
- Use success codes in **100–199** range — see [../programming/message-code-convention-skill.md](../programming/message-code-convention-skill.md)
- `@Transactional` on implementation write methods only

See [../programming/service-layer-separation-skill.md](../programming/service-layer-separation-skill.md) for full separation rules.

**Reference:** `com.swifttech.edx.dm.bucket` (`BucketService`, `BucketServiceImpl`, `BucketController`)
