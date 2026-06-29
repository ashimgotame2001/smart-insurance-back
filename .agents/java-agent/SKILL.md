---
name: smart-remittance-java-agent
description: >-
  Generates and reviews Java Spring Boot code for Smart Remittance using
  Platform → Product → Solution layering, OpenAPI-first APIs, and module-specific
  remittance rules. Use when implementing, reviewing, or refactoring Smart
  Remittance backend services, APIs, JPA entities, or remittance modules.
allowed-tools: Read, Grep, Glob, Write, Bash
---

# Smart Remittance Java Agent Skill

## Purpose

The Java Agent understands functional requirements, domain models, Figma flows, OpenAPI contracts, and Smart Remittance module structure to generate and review Java Spring Boot code.

The agent follows:

Platform Layer → Product Layer → Solution Layer

## Main Skill Areas

1. Functional Analysis Skill
2. Programming Skill
3. OpenAPI Skill
4. Review Skill
5. Traceability Skill
6. Code Generation Skill

## Technology

- Java 25
- Spring Boot 4.x
- OpenAPI
- Spring Data JPA
- Spring Data Projections
- PostgreSQL 18
- Jakarta Validation
- Spring Security
- REST
- WebClient
- gRPC
- Kafka/Event Bus
- Apache Camel
- JasperReports
- Micrometer
- Structured Logging
- Distributed Tracing
- Gradle Multi-module Project

## Skill Files

### Core
Read files from:

- `core/`

### Functional Analysis
Read files from:

- `functional-analysis/`

### Programming
Read files from:

- `programming/`

### OpenAPI
Read files from:

- `openapi/`

### Review
Read files from:

- `review/`

### Traceability
Read files from:

- `traceability/`

### Code Generation
Read files from:

- `code-generation/`

### Checklists
Read files from:

- `checklists/`

## Agent Rule

Before generating code, the Java Agent must understand:

- Functional requirement
- Domain model
- Figma or screen flow
- OpenAPI contract
- Platform/Product/Solution boundary
- Target Smart Remittance module
- Required API
- Required business rule
- Required validation
- Required traceability

## Do Not

The Java Agent must not:

- Put business logic inside controllers
- Put business validation or inline field mapping inside service impl — use `XxxValidationService` and MapStruct `XxxMapper` (or `XxxMappingService` for custom only)
- Return entities directly from APIs
- Hardcode Solution-specific rules inside Product modules
- Duplicate Platform utilities
- Ignore OpenAPI contracts
- Ignore validation
- Ignore pagination for list APIs
- Load full entities for list APIs when a projection is sufficient
- Ignore observability and traceability
- Scaffold Gradle modules, dependencies, or package structure — target module and model are manually configured in the SR repo

## Start Here

For most tasks, read these files first:

1. [module-map.md](module-map.md)
2. [core/platform-product-solution-boundary.md](core/platform-product-solution-boundary.md)
3. [functional-analysis/functional-analysis-skill.md](functional-analysis/functional-analysis-skill.md)
4. [programming/programming-skill.md](programming/programming-skill.md)
5. [programming/industry-coding-standards-skill.md](programming/industry-coding-standards-skill.md)
6. [programming/design-pattern-selection-skill.md](programming/design-pattern-selection-skill.md)
7. [programming/service-runtime-switching-skill.md](programming/service-runtime-switching-skill.md)
8. [openapi/openapi-first-skill.md](openapi/openapi-first-skill.md)
9. [review/review-skill.md](review/review-skill.md)
10. [traceability/traceability-skill.md](traceability/traceability-skill.md)

## Workflows

- Full feature delivery runbook: [end-to-end-workflow.md](end-to-end-workflow.md)

## Prompts and Templates

- Reusable agent commands: [prompts/](prompts/)
- Industry-standard code generation: [prompts/industry-standard-code-generation.md](prompts/industry-standard-code-generation.md)
- Generation formats: [templates/](templates/)
