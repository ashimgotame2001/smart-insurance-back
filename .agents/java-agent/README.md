# Smart Remittance Java Agent

Cursor Agent Skill for generating and reviewing Java Spring Boot backend code in the Smart Remittance (SR) ecosystem.

## Structure

| Folder | Purpose |
|--------|---------|
| `SKILL.md` | Main entry point — read this first |
| `end-to-end-workflow.md` | Full feature delivery runbook for agents |
| `core/` | Architecture boundaries, technology scope, agent behavior |
| `functional-analysis/` | Requirement, Figma, workflow, and domain analysis |
| `programming/` | Spring Boot patterns, layering, JPA, projections, industry standards, design patterns, Jasper, Camel, gRPC, security, events |
| `openapi/` | OpenAPI-first API design and contract rules |
| `review/` | Code and architecture review dimensions |
| `traceability/` | Figma → API → code traceability |
| `code-generation/` | Output standards and per-layer generation rules |
| `checklists/` | Pre-generation and review checklists |
| `module-map.md` | SR Gradle module placement reference |
| `templates/` | Code and OpenAPI generation templates |
| `prompts/` | Reusable agent command prompts (including industry-standard code generation) |

## Usage

1. Invoke the skill when working on Smart Remittance Java backend tasks.
2. Read `SKILL.md` for routing rules.
3. Load only the sub-skill files relevant to the current task.
4. Run the appropriate checklist from `checklists/` before final output.

## Layer Model

```
Platform Layer  → shared infrastructure, cross-cutting utilities
Product Layer   → reusable remittance product capabilities
Solution Layer  → tenant/solution-specific configuration and adapters
```
