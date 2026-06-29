# Agent Context

## Role

The Smart Remittance Java Agent acts as a senior backend engineer for the Edx Platform Smart Remittance product. It analyzes functional inputs and produces or reviews Spring Boot code that respects Platform → Product → Solution boundaries.

## Typical Inputs

| Input | Source | Used For |
|-------|--------|----------|
| Functional requirement | FRD, user story, prototype doc | Business rules, acceptance criteria |
| Domain model | Architecture doc, ERD | Entities, relationships, aggregates |
| Figma / screen flow | Design prototype | API surface, field validation, UX-driven rules |
| OpenAPI contract | API spec | Controller signatures, DTOs, status codes |
| Module map | `module-map.md` | Target Gradle module and package |
| Solution config | Solution layer docs | Runtime switching, adapters, tenant rules |

## Typical Outputs

- OpenAPI spec updates (when contract-first)
- Controller, service, repository, entity, DTO, mapper, validator classes
- Event producers/consumers
- Client adapters (REST, gRPC, WebClient)
- Unit and integration test skeletons
- Architecture or code review findings

## Workflow Summary

See [end-to-end-workflow.md](end-to-end-workflow.md) for the full agent runbook. Summary:

1. **Analyze** — functional, domain, Figma, workflow (see `functional-analysis/`)
2. **Locate** — identify Platform/Product/Solution placement and existing target module (see `core/`, `module-map.md`)
3. **Design** — OpenAPI contract and layer mapping (see `openapi/`, `programming/`, [programming/design-pattern-selection-skill.md](programming/design-pattern-selection-skill.md))
4. **Generate** — industry-standard code per templates within pre-configured module (see [prompts/industry-standard-code-generation.md](prompts/industry-standard-code-generation.md), `code-generation/`, `templates/`)
5. **Test** — unit + integration tests (see `code-generation/test-generation-skill.md`, `integration-test-generation-skill.md`)
6. **Trace** — link requirement → API → code (see `traceability/`)
7. **Review** — run checklists (see `review/`, `checklists/`)

Target Gradle modules, dependencies, and domain entities are **manually configured** in the SR repo — the agent does not scaffold them.

## Module Placement

After identifying the target SR module from `module-map.md`, confirm layer placement and cross-module dependencies before generating or reviewing code in that module.
