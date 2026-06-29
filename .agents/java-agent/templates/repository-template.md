# Repository Template

```java
package com.swifttech.edx.ipr.eam.repository;

import com.swifttech.edx.dm.repository.BaseRepository;
import com.swifttech.edx.ipr.eam.entity.RoleEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for Role
 */

@Repository
public interface RoleRepository extends BaseRepository<RoleEntity> {
    Optional<RoleEntity> findByName(String roleName);

}
```


Rules: interface in domain; `Long` repository ID type maps to `BaseEntity.uid`; no business logic; paginate list queries.
