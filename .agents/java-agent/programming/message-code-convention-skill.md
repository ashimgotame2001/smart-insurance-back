# Message Code Convention Skill

## Purpose

Define the standard message code format, numeric ranges, method-to-operation mapping, and module configuration used across Smart Remittance services for errors, success responses, and validation messages.

All codes are resolved via `MessageHelper` and returned in `GlobalResponse.code` and `CauseDetail.code`.

**Related:** [exception-handling-skill.md](exception-handling-skill.md), [validation-skill.md](validation-skill.md)

---

## Message Code Format

Every message code must follow:

```text
AAA-BBB-CRE-001
```

| Part | Description | Rules |
|------|-------------|-------|
| **AAA** | Module name | Exactly 3 characters; unique across platform |
| **BBB** | Service name | Exactly 3 characters; usually same as module name; unique |
| **CRE** | Resource / operation | Derived from method name (e.g. `CRE`, `UPD`, `GET`) |
| **001** | Code number | Depends on message category (see ranges below) |

**Full example:** `RMS-RMS-CRE-001`

---

## Code Number Ranges

| Category | Description | Range |
|----------|-------------|-------|
| **Error messages** | System or business logic errors | `001` – `099` |
| **Success messages** | Successful operations | `100` – `199` |
| **Validation messages** | Input or field validation | `200` – `299` |

### Examples by category

| Code | Category | Meaning |
|------|----------|---------|
| `AAA-BBB-CRE-001` | Error | Error while validating id on create |
| `AAA-BBB-CRE-101` | Success | Record created successfully |
| `AAA-BBB-CRE-201` | Validation | Required field missing on create |
| `AAA-BBB-UPD-002` | Error | Error during update |
| `AAA-BBB-GET-101` | Success | Record retrieved successfully |

---

## Method Naming Convention

Methods must use **camelCase** and clearly reflect their purpose.

The **operation code** (third segment of the message code) is derived automatically from the method name:

| Method name pattern | Operation code | Description |
|---------------------|----------------|-------------|
| `create*()` | `CRE` | Create operation |
| `update*()` | `UPD` | Update operation |
| `update*Status()` | `UST` | Update status operation |
| `find*ById()` | `GET` | Retrieve single record |
| `findAll*()` | `LST` | Retrieve list of records |
| `find*PaginatedData()` | `PLS` | Retrieve paginated list |
| No match | `UNK` | Unknown operation (default) |

### Example message codes by method

| Method | Generated code | Description |
|--------|----------------|-------------|
| `createMessage()` | `AAA-BBB-CRE-001` | Error thrown while validating id |
| `createMessage()` | `AAA-BBB-CRE-101` | Message created successfully |
| `deleteMessage()` | `AAA-BBB-UNK-101` | Unknown operation — defaults to `UNK` |

---

## Rules for Agents and Developers

1. Assign **AAA** and **BBB** from module configuration before generating codes
2. Name service/controller methods so operation codes derive correctly (`create`, `update`, `findById`, etc.)
3. Pick the numeric suffix from the correct range (error / success / validation)
4. Use the same code in `GlobalException`, validation message keys, and `MessageHelper` lookups
5. Do not invent ad-hoc codes outside `AAA-BBB-OPR-NNN` format
6. Register new module/service abbreviations in configuration before use

---

## Message Configuration (YAML)

Module and service abbreviations are defined in application configuration.

### Template structure

```yaml
message:
  management:
    moduleName: AAA          # 3-character module code (template placeholder)
    serviceName:
      ApplicationModule: APM
      Application: APP
      ApplicationResource: APR
      ApplicationService: APS
      DefaultLabel: DLB
      DefaultMessage: DMG
      Message: MSG
      Label: LBL
      LocaleProfile: LPF
      LocaleProperty: LPR

api:
  module: module-name          # actual module slug for API routing/docs
```

### Purpose

| Section | Purpose |
|---------|---------|
| `message.management.moduleName` | 3-character module identifier (`AAA` in code format) |
| `message.management.serviceName.*` | Service-type abbreviations for message keys, i18n, and lookups |
| `api.module` | Module slug for API scoping (e.g. `/api/remittance-services/...`) |

### Service name keys

| Key | Abbreviation | Typical use |
|-----|--------------|-------------|
| `ApplicationModule` | `APM` | Application module entities |
| `Application` | `APP` | Application-level messages |
| `ApplicationResource` | `APR` | Resource definitions |
| `ApplicationService` | `APS` | Service-layer messages |
| `DefaultLabel` | `DLB` | Default UI labels |
| `DefaultMessage` | `DMG` | Default message templates |
| `Message` | `MSG` | General messages |
| `Label` | `LBL` | Label resources |
| `LocaleProfile` | `LPF` | Locale profile config |
| `LocaleProperty` | `LPR` | Locale property config |

### Example (Remittance module)

```yaml
message:
  management:
    moduleName: RMS          # Remittance Services
    serviceName:
      ApplicationModule: APM
      Application: APP
      ApplicationResource: APR
      ApplicationService: APS
      DefaultLabel: DLB
      DefaultMessage: DMG
      Message: MSG
      Label: LBL
      LocaleProfile: LPF
      LocaleProperty: LPR

api:
  module: remittance-services
```

Possible message key patterns from this setup:

- `MSG_APP_001`
- `DLB_APM_TITLE`
- `RMS-RMS-CRE-101`

---

## Usage in Code

### Throw business error

```java
throw new GlobalException("RMS-RMS-CRE-001", HttpStatus.UNPROCESSABLE_CONTENT);
```

### Success response

```java
return ServiceResponseBuilder.buildSuccessResponseWithMessage(
    "RMS-RMS-CRE-101",
    MessageHelper.getMessage("RMS-RMS-CRE-101")
);
```

### Validation message key

Use validation range `200–299` and align with `CauseDetail.code` in `GlobalResponse.errorDetail`:

```java
// validation annotation or custom validator message key
"{validation.RMS-RMS-CRE-201}"
```

---

## Smart Remittance Module Codes

Map `sr-*` Gradle modules to 3-character codes in `module-map.md` and message config. Example:

| Gradle module | Suggested `AAA` / `BBB` |
|---------------|-------------------------|
| `sr-remittance-services` | `RMS` |
| `sr-customer-management` | `CUS` |
| `sr-beneficiary-management` | `BEN` |
| `sr-payment-services` | `PAY` |

Confirm actual codes against project configuration before generating messages.

---

## Checklist

- [ ] Module code (AAA) is exactly 3 characters and registered
- [ ] Service code (BBB) is exactly 3 characters and unique
- [ ] Method name derives correct operation code (or `UNK` is intentional)
- [ ] Numeric suffix matches category range (001–099 / 100–199 / 200–299)
- [ ] Code registered in message management / `MessageHelper` source
- [ ] OpenAPI and `GlobalResponse` use the same code string
