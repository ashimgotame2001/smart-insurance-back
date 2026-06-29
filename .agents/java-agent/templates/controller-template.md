# Controller Template

```java
package com.swifttech.edx.ipr.eam.adapter;

import com.swifttech.edx.dm.exception.GlobalException;
import com.swifttech.edx.dm.payload.request.StatusUpdateRequest;
import com.swifttech.edx.dm.payload.response.GlobalResponse;
import com.swifttech.edx.ipr.eam.annotation.UserManagementRestController;
import com.swifttech.edx.ipr.eam.model.Role;
import com.swifttech.edx.ipr.eam.model.request.BulkRoleUpdateRequest;
import com.swifttech.edx.ipr.eam.model.request.RoleDataRequest;
import com.swifttech.edx.ipr.eam.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@UserManagementRestController
@RequiredArgsConstructor
public class RoleController{
    private final RoleService service;

    @PostMapping("role/create")
    public ResponseEntity<GlobalResponse> createRole(@RequestBody @Valid Role request) throws GlobalException{
        return ResponseEntity.ok(service.createRole(request));
    }

    @PutMapping("role/update/{id}")
    public ResponseEntity<GlobalResponse> updateRole(@RequestBody @Valid Role request, @PathVariable Long id) throws GlobalException{
        return ResponseEntity.ok(service.updateRole(request, id));
    }
    
    @PatchMapping("role/update-status/{id}")
    public ResponseEntity<GlobalResponse> updateRoleStatus(@RequestBody StatusUpdateRequest request, @PathVariable Long id) throws GlobalException{
        return ResponseEntity.ok(service.updateRoleStatus(request, id));
    }

    @GetMapping("role/{id}")
    public ResponseEntity<GlobalResponse> findRoleById(@PathVariable Long id) throws GlobalException{
        return ResponseEntity.ok(service.findRoleById(id));
    }

    @PostMapping("role/list")
    public ResponseEntity<GlobalResponse> findRolePaginatedData(@Valid @RequestBody RoleDataRequest request) throws GlobalException{
        return ResponseEntity.ok(service.findRolePaginatedData(request));
    }

    @GetMapping("role/find-all")
    public ResponseEntity<GlobalResponse> findAllRole() throws GlobalException{
        return ResponseEntity.ok(service.findAllRole());
    }
}
```

Rules:

- Inject **service interface**, not implementation
- Return **`ResponseEntity<GlobalResponse<T>>`** — delegate to service; no business logic
- Use **`ResponseEntity.ok(...)`** for success unless OpenAPI specifies 201
- Declare **`throws GlobalException`** — handled by `GlobalExceptionHandlerBaseController`
- Wrap service list/plain results with **`MessageHelper.buildSuccessResponseWithData`** in adapter when service does not return `GlobalResponse`
- Binary/stream endpoints may return `ResponseEntity<InputStreamResource>` with headers from service response model

**Reference:** `com.swifttech.edx.dm.bucket.adapter.BucketController`
