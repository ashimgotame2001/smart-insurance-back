# Agent Behavior Rules

## Before Any Code Generation

1. Read [module-map.md](../module-map.md) and identify target module
2. Read [platform-product-solution-boundary.md](platform-product-solution-boundary.md)
3. Confirm functional requirement, domain model, and OpenAPI contract (or create contract first)
4. Run [pre-generation-checklist.md](../checklists/pre-generation-checklist.md)

## During Generation

- Load [industry-coding-standards-skill.md](../programming/industry-coding-standards-skill.md) and [design-pattern-selection-skill.md](../programming/design-pattern-selection-skill.md) — evaluate Creational, Structural, and Behavioral patterns
- Classify the feature type and apply required design pattern(s) by category before writing code
- Match existing project naming and package conventions
- Generate smallest complete vertical slice (controller → service → validation → mapper → repo → entity → DTO)
- Service impl orchestrates: **`validationService.createValidate` → `mapper.toEntity` → `repository.save`**
- Include validation service, exception handling, and observability hooks
- Add traceability comments or doc references where business rules apply
- Use templates from [../templates/](../templates/)

## During Review

- Load [review-skill.md](../review/review-skill.md) and relevant sub-reviews
- Report findings with severity: **Critical**, **Major**, **Minor**
- Reference file paths and line evidence

## Communication

- State assumptions explicitly when inputs are incomplete
- Ask for missing OpenAPI or Figma reference before guessing API shape
- Summarize layer placement decisions for new code

## Must Always

- Return **`GlobalResponse`** from services; adapters return `ResponseEntity<GlobalResponse<T>>`
- Paginate list endpoints
- Validate request bodies with Jakarta Validation
- Propagate trace/correlation IDs in logs
- Map exceptions to standard Platform error responses via `GlobalException`

## Must Never

- Skip OpenAPI when adding or changing an API
- Put SQL or JPA queries in controllers
- Hardcode credentials or secrets
- Ignore Solution boundary and embed tenant logic in Product code
- Generate code without identifying the target SR module
- Scaffold Gradle modules, dependencies, package trees, or `module-map.md` entries — these are manually configured
