# Generate New Module

Use the Smart Remittance Java Agent skill.

## Task

Generate initial API and class skeletons **inside an existing, manually configured** `sr-*` module.

## Preconditions (manual — out of agent scope)

The following must already exist in the target SR repository before using this prompt:

- Gradle module registered (`settings.gradle`, `build.gradle`)
- Module dependencies added manually
- Domain model / entities inserted as required
- Package layout matching the target module's existing conventions

Do **not** scaffold Gradle files, invent package trees, or update `module-map.md` unless explicitly requested.

## Inputs

- Target SR module path (existing):
- APIs to expose:
- Reference classes in the same module (for naming/package conventions):

## Instructions

1. Confirm module from [module-map.md](../module-map.md) and inspect **existing** package layout in the target repo
2. Match naming, packages, and patterns from sibling classes ([spring-boot-layered-architecture-skill.md](../programming/spring-boot-layered-architecture-skill.md))
3. Generate initial OpenAPI + skeleton classes only within the pre-configured module

## Output

Generated class list with paths + OpenAPI draft — no Gradle or module-map changes
