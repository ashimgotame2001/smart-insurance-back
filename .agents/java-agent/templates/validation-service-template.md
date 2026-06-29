# Validation Service Template

Business-rule validation layer — injected into `XxxServiceImpl`. Separate from Jakarta `@Constraint` validators on request DTOs.

## Class (`validator/CustomerValidationService.java`)

```java
package com.swifttech.edx.ipr.eam.service.validation;

import com.swifttech.edx.dm.am.enums.ErrorCodeEnum;
import com.swifttech.edx.dm.exception.GlobalException;
import com.swifttech.edx.ipr.eam.entity.RoleEntity;
import com.swifttech.edx.ipr.eam.model.Role;
import com.swifttech.edx.ipr.eam.repository.RoleRepository;

import java.util.Objects;
@Service
@RequiredArgsConstructor
public class RoleValidationService {

    private final RoleRepository roleRepository;

    public void validateRoleCreation(Role role) throws GlobalException {
        validateIfRoleWithSameNameExists(role.getName());
    }

    public void validateRoleUpdate(Role role, RoleEntity existingRole) throws GlobalException {

        if (!Objects.equals(existingRole.getName(), role.getName())) {
            validateIfRoleWithSameNameExists(role.getName());
        }
    }

    private void validateIfRoleWithSameNameExists(String roleName) throws GlobalException {

        if (roleName == null || roleName.isBlank()) {
            throw new GlobalException(ErrorCodeEnum._002.getMessage());
        }

        if (roleRepository.findByName(roleName).isPresent()) {
            throw new GlobalException(ErrorCodeEnum._003.getMessage());
        }
    }
}
```

## Rules

- One `{Aggregate}ValidationService` per aggregate root
- Package: `validator/` (same module as the aggregate)
- Method names: **`createValidate`**, **`updateValidate`**, **`deleteValidate`**
- Return **`void`**; throw **`GlobalException`** with message codes from [message-code-convention-skill.md](../programming/message-code-convention-skill.md)
- Reference business rule IDs in JavaDoc (`BR-CUS-001`)
- May inject repositories for existence, uniqueness, and state checks
- Do **not** map DTOs or call `save` — validation only

## Related

- [service-template.md](service-template.md) — orchestration in `ServiceImpl`
- [validator-template.md](validator-template.md) — Jakarta DTO `@Constraint` validators
- [../programming/service-layer-separation-skill.md](../programming/service-layer-separation-skill.md)
