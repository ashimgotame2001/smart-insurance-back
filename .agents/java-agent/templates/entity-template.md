# Entity Template

**Reference:** `com.swifttech.edx.dm.entity.BaseEntity`

## BaseEntity (inherited — do not redeclare)

| Field | Column | Notes |
|-------|--------|-------|
| `uid` | (default) | `@Id`, `GenerationType.IDENTITY` — primary key |
| `version` | — | `@Version` optimistic locking |
| `createdAt` | `created_at` | `@CreationTimestamp` |
| `lastModifiedAt` | `last_modified_at` | `@LastModifiedDate` |
| `createdBy` | `created_by_id` | `@CreatedBy` |
| `lastModifiedBy` | `modified_by_id` | `@LastModifiedBy` |

`BaseEntity` is `@MappedSuperclass` with `AuditingEntityListener` — enable JPA auditing in module config.

## Entity example

```java
package com.swifttech.edx.ipr.eam.entity;

import com.swifttech.edx.dm.am.entity.ApplicationResourceEntity;
import com.swifttech.edx.dm.entity.BaseEntity;
import com.swifttech.edx.dm.enums.StatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Entity class for Role
 */
@Entity
@Table(name="pr_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity extends BaseEntity {

    @Column(
            name = "name"
    )
    private String name;
    @Column(
            name = "description"
    )
    private String description;

    @Column(
            name = "status"
    )
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<ApplicationResourceEntity> permissions;


    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

}
```

## Rules

- **Extend `BaseEntity`** — do **not** declare `@Id`, `uid`, `version`, or audit fields in the entity
- Import: `com.swifttech.edx.dm.entity.BaseEntity`
- Explicit `@Table`, `@Column` for domain fields
- `@Enumerated(EnumType.STRING)` for status fields
- Repository ID type is `Long` (maps to inherited `uid`)

**Reference entity:** `com.swifttech.edx.dm.entity.FileEntity`
