# Smart Insurance — Backend Context

## Permission & Authorization Architecture

### Data Model
```
User (application_users)
 ├── roles (user_roles) ─── ManyToMany, EAGER      ← direct role assignment (additional only)
 ├── userGroup (FK group_id) ─── ManyToOne, EAGER   ← primary role source
 │    └── UserGroup (user_groups)
 │         └── roles (user_group_roles) ─── ManyToMany, EAGER
 │              └── Role (roles)
 │                   └── permissions (role_permissions) ─── ManyToMany, EAGER
 │                        └── Permission (permissions)
```

### Priority (AuthenticationService calls `UserDetailsService` which merges both)
1. **Group roles** (`user.getUserGroup().getRoles()`) = primary — always applied
2. **User direct roles** (`user.getRoles()`) = additional — only for extra permissions on top of group

### User Registration
- `register()` creates user with **empty** direct roles (`Set<Role> roles = new HashSet<>()`)
- User is assigned to a UserGroup (mandatory)
- All permissions come from group roles (resolved dynamically at auth time)

### Super Admin Detection (`ApplicationConfig.userDetailsService()`)
```java
boolean isSuperAdmin = user.getRoles().stream()
    .anyMatch(role -> "ROLE_SUPER_ADMIN".equals(role.getCode()))
    || (user.getUserGroup() != null && user.getUserGroup().getRoles().stream()
        .anyMatch(role -> "ROLE_SUPER_ADMIN".equals(role.getCode())));
```
- If super admin: grants **all** permissions from `permissionRepository.findAll()`
- If normal user: merges direct + group roles, collects all unique permission codes as authorities

### Authority Flow
1. `POST /api/v1/auth/authenticate` — `AuthenticationManager` calls `UserDetailsService`
2. `UserDetailsService` builds Spring Security `User` with permission codes as `authorities`
3. `JwtService.generateToken()` — creates JWT with username + extra claims (companyId, branchId, branchCode, branchName)
4. Subsequent requests: `JwtAuthenticationFilter` extracts JWT → loads UserDetails → sets `SecurityContext` with authorities
5. `@PreAuthorize("hasAuthority('PERM_XXX')")` checks against authorities from `SecurityContext`

### Key Files
| File | Purpose |
|------|---------|
| `ApplicationConfig.java` | `UserDetailsService` bean — role merge + authority building |
| `JwtAuthenticationFilter.java` | JWT extraction, validation, SecurityContext population |
| `SecurityConfig.java` | Endpoint security rules, CORS, CSP, filter chain |
| `AuthenticationService.java` | Login, register, permissions API, MFA, force password change |
| `CheckerMakerAspect.java` | AOP aspect for checker-maker approval flow |

---

## Error Codes

### Convention
- **Format:** `MODULE-NNN=Message with {0} {1} placeholders`
- All codes are globally unique across the entire application

### Module Prefixes & HTTP Status Mapping
| Prefix | HTTP Status | Description |
|--------|-------------|-------------|
| `SYS-*` | 500 | System/internal errors |
| `AUTH-*` | 401 | Authentication failures |
| `CHK-*` | 403 | Forbidden / authorization failures |
| All others | 400 | Validation / business logic failures |

### Complete Error Code Reference

**SYS (System)**
- `SYS-001` — Internal server error
- `SYS-002` — Authentication required
- `SYS-003` — Invalid request
- `SYS-004` — Resource not found

**AUTH (Authentication)**
- `AUTH-001` — Invalid credentials
- `AUTH-002` — Account disabled
- `AUTH-003` — Username already exists
- `AUTH-004` — Email already exists
- `AUTH-005` — Authentication required for this operation
- `AUTH-006` — Refresh token expired
- `AUTH-007` — Invalid refresh token
- `AUTH-008` — Account locked
- `AUTH-009` — Current password is incorrect
- `AUTH-010` — Force password change required
- `AUTH-011` — Login not allowed at this time ({0}-{1})
- `AUTH-012` — Login not allowed on this day

**USR (User Management)**
- `USR-001` — User not found
- `USR-002` — Username already exists
- `USR-003` — Email already exists
- `USR-004` — User not found by username
- `USR-005` — Group is mandatory

**ROL (Roles)**
- `ROL-001` — Role not found
- `ROL-002` — Role name already exists
- `ROL-003` — Role code already exists

**GRP (User Groups)**
- `GRP-001` — Group not found
- `GRP-002` — Group name already exists (no, this is GRP-004)
- `GRP-003` — Group code already exists (no, this is GRP-005)
- `GRP-004` — Group name already exists
- `GRP-005` — Group code already exists

**PWP (Password Policy)**
- `PWP-001` — Password policy not found
- `PWP-002` — Policy name already exists
- `PWP-003` — Policy code already exists
- `PWP-004` — Only one password policy allowed
- `PWP-005` — Cannot delete default password policy

**MFA (MFA Policy)**
- `MFA-001` — MFA policy not found
- `MFA-002` — Only one default policy allowed
- `MFA-003` — Policy with this group already exists
- `MFA-004` — Group not found
- `MFA-005` — MFA not enabled for user
- `MFA-006` — Invalid verification code
- `MFA-007` — User not found for MFA token

**BRN (Branches)**
- `BRN-001` — Branch not found
- `BRN-002` — Branch code already exists

**CMP (Company Profile)**
- `CMP-001` — Company profile not found
- `CMP-002` — Company profile already exists
- `CMP-003` — Company setup already completed

**CTY (Countries)**
- `CTY-001` — Country not found
- `CTY-002` — Country name already exists
- `CTY-003` — Country code already exists

**PRV (Provinces)**
- `PRV-001` — Province not found
- `PRV-002` — Province code already exists

**DST (Districts)**
- `DST-001` — District not found
- `DST-002` — District code already exists

**MUN (Municipalities)**
- `MUN-001` — Municipality not found
- `MUN-002` — Municipality code already exists

**WRD (Wards)**
- `WRD-001` — Ward not found
- `WRD-002` — Ward code already exists

**RGN (Regions)**
- `RGN-001` — Region not found
- `RGN-002` — Region code already exists

**CUR (Currencies)**
- `CUR-001` — Currency not found
- `CUR-002` — Currency code already exists

**LNG (Languages)**
- `LNG-001` — Language not found
- `LNG-002` — Language code already exists

**DAT (Date Formats)**
- `DAT-001` — Date format not found
- `DAT-002` — Date format already exists

**TIM (Time Formats)**
- `TIM-001` — Time format not found
- `TIM-002` — Time format already exists

**UTP (User Types)**
- `UTP-001` — User type not found
- `UTP-002` — User type name already exists
- `UTP-003` — User type code already exists

**BNK (Banks)**
- `BNK-001` — Bank not found
- `BNK-002` — Bank code already exists

**BKB (Bank Branches)**
- `BKB-001` — Bank branch not found
- `BKB-002` — Bank branch code already exists

**HSP (Hospitals)**
- `HSP-001` — Hospital not found
- `HSP-002` — Hospital code already exists

**VBR (Vehicle Brands)**
- `VBR-001` — Vehicle brand not found
- `VBR-002` — Vehicle brand code already exists

**VMO (Vehicle Models)**
- `VMO-001` — Vehicle model not found
- `VMO-002` — Vehicle model code already exists

**RIN (Reinsurance Companies)**
- `RIN-001` — Reinsurance company not found
- `RIN-002` — Reinsurance company code already exists

**ACP (Accounting Periods)**
- `ACP-001` — Accounting period not found
- `ACP-002` — Accounting period code already exists
- `ACP-003` — Invalid accounting period dates

**EXR (Exchange Rates)**
- `EXR-001` — Exchange rate not found

**CMS (Commission Slabs)**
- `CMS-001` — Commission slab not found

**LKV (Lookup Values)**
- `LKV-001` — Lookup value not found
- `LKV-002` — Lookup value already exists for this category

**NTC (Notification Channels)**
- `NTC-001` — Notification channel not found
- `NTC-002` — Channel name already exists
- `NTC-003` — Channel code already exists

**NTT (Notification Templates)**
- `NTT-001` — Notification template not found
- `NTT-002` — Template code already exists

**NTK (Notification Keywords)**
- `NTK-001` — Notification keyword not found
- `NTK-002` — Keyword name already exists
- `NTK-003` — Keyword code already exists

**MNU (Menus)**
- `MNU-001` — Menu not found
- `MNU-002` — Menu code already exists

**DOC (Documents)**
- `DOC-001` — File upload failed
- `DOC-002` — File retrieval failed
- `DOC-003` — File deletion failed
- `DOC-004` — Document not found

**BLK (Bulk Upload)**
- `BLK-001` — Field set failed
- `BLK-002` — Unknown entity type

**CHK (Checker-Maker)**
- `CHK-001` — Only creator can modify this resource

---

## Success Codes

### Convention
- **Format:** `MODULE-SUC-NNN=Message` or `SUC-NNN=Message` for generic codes

### Complete Success Code Reference
- `SUC-001` — Operation completed successfully
- `SUC-002` — Resource created successfully
- `SUC-003` — Resource updated successfully
- `SUC-004` — Resource deleted successfully
- `AUTH-SUC-001` — Login successful
- `AUTH-SUC-002` — MFA verification successful
- `AUTH-SUC-003` — Token refreshed successfully
- `AUTH-SUC-004` — Logged out successfully
- `AUTH-SUC-005` — User registered successfully
- `AUTH-SUC-006` — User updated successfully
- `AUTH-SUC-007` — Password changed successfully
- `MFA-SUC-001` — MFA setup data generated
- `MFA-SUC-002` — MFA enabled successfully
- `MFA-SUC-003` — MFA disabled successfully
- `DOC-SUC-001` — Document uploaded successfully
- `DOC-SUC-002` — Document retrieved successfully
- `DOC-SUC-003` — Document deleted successfully

---

## Exception Handling

### GlobalException
- Extends `RuntimeException`
- Constructor: `GlobalException(String code, Object... args)`
- `code` = error code (e.g., `"USR-001"`)
- `args` = dynamic message placeholders for `{0}`, `{1}` in `error.properties`

### GlobalExceptionHandler (@RestControllerAdvice)
- `handleGlobalException(GlobalException ex)`:
  - Resolves message: `errorMessageSource.getMessage(code, args, defaultCode, locale)`
  - Status mapping: `AUTH-*`→401, `CHK-*`→403, `SYS-*`→500, all others→400
  - Returns `ApiResponse.error(code, message)` with mapped HTTP status
- `handleGeneralException(Exception ex)`:
  - Catches all unhandled exceptions
  - Returns `SYS-001` with HTTP 500

### ApiResponse DTO
```json
{
  "success": true/false,
  "code": "SUC-001",
  "message": "Operation completed successfully.",
  "data": { ... }
}
```
- Success: `ApiResponse.success(code, message, data)`
- Error: `ApiResponse.error(code, message)`
- Builder: `SuccessResponseBuilder.buildSuccessResponse(code, data)` — auto-resolves message from `success.properties`

### Throwing Conventions
- All business exceptions use `GlobalException`, never raw `RuntimeException`
- First argument is always the error code string (e.g., `"BRN-001"`)
- Subsequent arguments are message placeholders
- Success responses use `SuccessResponseBuilder.buildSuccessResponse("SUC-001", data)`

### Critical Rules
- Never hardcode error/success messages in Java code — always use `.properties` files
- All error codes must be globally unique across modules
- `AUTH-*` codes always return 401 (not 400, not 403)

---

## Security Filters (Ordered)

| Order | Filter | Purpose |
|-------|--------|---------|
| 1 | `XssFilter` | Wraps all requests in `XssRequestWrapper` — strips HTML from params/headers via Jsoup |
| 2 | `RateLimitingFilter` | Bucket4j token bucket: auth→5/min, swagger→10/min, others→60/min per client IP |

### SecurityConfig (Spring Security)
- `permitAll`: `/api/v1/auth/authenticate`, `/api/v1/auth/refresh-token`, swagger paths
- `.anyRequest().authenticated()` — all other endpoints require valid JWT
- JWT filter: `JwtAuthenticationFilter` (before `UsernamePasswordAuthenticationFilter`)
- CSP: `default-src 'self'` with `'unsafe-inline'` for styles (Ant Design requirement)
- CORS: all origin patterns, standard methods and headers, credentials allowed

---

## MFA (Multi-Factor Authentication)

### TOTP Implementation
- Google/Microsoft Authenticator compatible (Time-based One-Time Password)
- `MfaService` / `MfaServiceImpl` handles TOTP operations (secret generation, QR code, code verification)
- `MfaPolicy` entity with `@ManyToOne userGroup` — two-tier policy: default (applies to all groups) + group-specific overrides (higher priority)

### MFA Login Flow
1. `authenticate()` → checks `mfaService.isMfaEnabledForUser(user)` → if enabled, returns `mfaRequired=true` + `mfaToken` (5-min JWT challenge)
2. Frontend shows MFA code input
3. `verifyMfa(mfaToken, code)` → validates code against user's stored `mfaSecret` → issues full JWT

---

## Force Password Change

### Flow
1. New users created with `forcePasswordChange=true`
2. `authenticate()` → `buildFullAuthResponse()` → sets `forcePasswordChange` flag in response
3. Frontend redirects to `/change-password`
4. `POST /api/v1/auth/change-password` → verifies current password, encodes new, sets `forcePasswordChange=false`
5. Tokens cleared, user redirected to `/login`

### Important
- `forcePasswordChange` check happens **after** authentication (in response), NOT during `UserDetailsService` loading
- This prevents `InternalAuthenticationServiceException` wrapping

---

## Database Schema Notes
- `User.roles` = EAGER (loaded with user via `user_roles` join table)
- `User.userGroup` = EAGER (loaded with user)
- `UserGroup.roles` = EAGER (loaded with group via `user_group_roles` join table)
- All many-to-many relationships use `Set<>` (not `List`) to avoid Hibernate bag issues
- `token_blacklist.token` = `varchar(2000)` (JWTs exceed default 255)
