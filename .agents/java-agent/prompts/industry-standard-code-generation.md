# Industry-Standard Code Generation

Use the Smart Remittance Java Agent skill.

## Task

Generate or refactor Java Spring Boot code that meets **industry-level coding standards** and applies **feature-appropriate design patterns** for Smart Remittance.

## Inputs

- Feature description / user story / FRD:
- Target SR module(s):
- OpenAPI draft (if API work):
- Integration details (if external systems involved):
- Reference classes in target module (for convention matching):

## Mandatory Standards

Before writing code, load and follow:

1. [industry-coding-standards-skill.md](../programming/industry-coding-standards-skill.md) — SOLID, clean code, Java/Spring conventions, testing, logging, security
2. [design-pattern-selection-skill.md](../programming/design-pattern-selection-skill.md) — select required pattern(s) by feature type
3. [programming-skill.md](../programming/programming-skill.md) — SR vertical slice and layer rules
4. [code-generation-output-standard.md](../code-generation/code-generation-output-standard.md) — deliverable package and quality bar

## Instructions

1. **Analyze** the feature and classify its type (CRUD, integration, event, tenant variant, orchestration, etc.)
2. **Select patterns systematically** by GoF category — evaluate Creational → Structural → Behavioral using [design-pattern-selection-skill.md](../programming/design-pattern-selection-skill.md); state chosen pattern(s), category, and rationale before coding
3. **Apply industry standards** — constructor injection, layered separation, Platform utilities, proper error handling, observability hooks
4. **Match conventions** — inspect existing module code; align naming, packages, and patterns with sibling classes
5. **Generate** the smallest complete vertical slice per capability
6. **Test** — unit tests for validation and service orchestration; slice/integration tests where behavior spans layers
7. **Self-review** against [code-review-checklist.md](../checklists/code-review-checklist.md) and industry standards gate

## Required in Output

1. **Pattern decision** — feature type; pattern matrix by category (Creational / Structural / Behavioral) with implementing classes
2. **Standards compliance summary** — brief note on SOLID layering, validation separation, error handling, and test coverage
3. **Generated files** — path list with one-line description each
4. **OpenAPI** — if API work
5. **Traceability matrix** — Req → API/Event → Class/Method
6. **Review notes** — any remaining Minor findings

## Do Not

- Skip pattern selection — every feature must map to at least the baseline layered + repository + mapper patterns
- Over-engineer with unnecessary abstractions or patterns not justified by the feature type
- Put business logic in controllers or inline validation/mapping in service impl
- Return entities from APIs or log sensitive data
- Deviate from SR Platform → Product → Solution boundaries

## Related Prompts

- Full feature: [generate-new-feature.md](generate-new-feature.md)
- Single API: [generate-new-api.md](generate-new-api.md)
- Refactor: [refactor-module.md](refactor-module.md)
- Review only: [review-code.md](review-code.md)
