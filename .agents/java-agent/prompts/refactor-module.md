# Refactor Module

Use the Smart Remittance Java Agent skill.

## Task

Refactor an SR module for clarity, performance, or maintainability without changing external API contracts.

## Inputs

- Module name:
- Refactor goals:
- Constraints (no API breaks, deadline, etc.):

## Instructions

1. Map current package structure and dependencies
2. Identify safe internal refactors (extract service, split validator, reduce N+1)
3. Preserve OpenAPI contract unless explicitly allowed to change
4. Update tests alongside refactor
5. Run review checklists

## Output

Refactor summary + incremental change set + test updates
