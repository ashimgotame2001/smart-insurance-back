# Service Interface Template

```java
package com.swifttech.edx.ipr.eam.service;

import com.swifttech.edx.dm.exception.GlobalException;
import com.swifttech.edx.dm.payload.request.StatusUpdateRequest;
import com.swifttech.edx.dm.payload.response.GlobalResponse;
import com.swifttech.edx.ipr.eam.model.Role;
import com.swifttech.edx.ipr.eam.model.request.BulkRoleUpdateRequest;
import com.swifttech.edx.ipr.eam.model.request.RoleDataRequest;

import java.util.List;

public interface RoleService{

    GlobalResponse createRole(Role request) throws GlobalException;
    GlobalResponse updateRole(Role request, Long id) throws GlobalException;
    GlobalResponse updateRolesInBulk(List<BulkRoleUpdateRequest> requests) throws GlobalException;
    GlobalResponse updateRoleStatus(StatusUpdateRequest request, Long id) throws GlobalException;
    GlobalResponse findRoleById(Long id) throws GlobalException;
    GlobalResponse findRolePaginatedData(RoleDataRequest request) throws GlobalException;
    GlobalResponse findAllRole() throws GlobalException;
}
```

Rules:

- Define **interface** in `application` (or `service`) package
- Command/query methods that surface to REST return **`GlobalResponse<T>`**
- Declare `throws GlobalException` on interface methods
- Use request/response **models** from `model.request` / `model.response`
- Infrastructure-only helpers (e.g. `StorageService`) may return plain types — not exposed via REST adapter
