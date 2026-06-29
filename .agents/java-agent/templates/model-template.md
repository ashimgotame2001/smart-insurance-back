# Model Template

```java
package com.swifttech.edx.ipr.eam.model;

import com.swifttech.edx.ipr.eam.model.request.PermissionRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * DTO for Role
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @NotBlank(message = "{201}")
    @NotNull(message = "{201}")
    private String name;
    @NotBlank(message = "{202}")
    @NotNull(message = "{202}")
    private String description;
    private Set<PermissionRequest> permissions;

}
```

Rules: validation on request models only; no JPA annotations; use `uid` for entity primary key in responses; place in `model.request` / `model.response` packages.
