# Functional Analysis Skill

## Purpose

Extract implementable backend requirements from functional documents, user stories, prototypes, and design artifacts before writing Java code.

## Inputs

- Functional requirement document (FRD) or user story
- Domain model / ERD
- Figma or screen flow (optional but recommended)
- Existing OpenAPI specs
- Edx Builder output (if available)

## Analysis Workflow

### Step 1 — Scope the feature

- Identify bounded context and target SR module ([module-map.md](../module-map.md))
- List actors, triggers, and outcomes
- Mark in-scope vs out-of-scope behavior

### Step 2 — Extract capabilities

For each capability, capture:

| Field | Description |
|-------|-------------|
| Capability ID | Stable reference (e.g. `REM-SEND-001`) |
| Description | What the system must do |
| Preconditions | Required state before execution |
| Postconditions | Expected state after success |
| Business rules | Validation, calculations, limits |
| Error cases | Failure scenarios and user-visible messages |

### Step 3 — Map to backend artifacts

| Requirement element | Backend artifact |
|--------------------|------------------|
| User action on screen | REST endpoint or command |
| Form fields | Request DTO fields + validation |
| List | Paginated GET with filters; use summary projection |
| Status transitions | Entity state enum + service methods |
| Notifications | Domain events |
| External system call | Client adapter |

### Step 4 — Identify gaps

Flag missing information:

- Unspecified validation rules
- Ambiguous status transitions
- Missing error handling for edge cases
- Undefined integration endpoints

## Output

Produce a **Functional Analysis Summary** containing:

1. Target module and layer placement
2. Capability list with business rules
3. Proposed API operations (names only; detail in OpenAPI skill)
4. Entity candidates and relationships
5. Events and integrations
6. Open questions for stakeholders

## Sub-Skills

| Topic | File |
|-------|------|
| Figma / UI flows | [figma-analysis-skill.md](figma-analysis-skill.md) |
| Domain model | [domain-model-analysis-skill.md](domain-model-analysis-skill.md) |
| Workflows | [workflow-analysis-skill.md](workflow-analysis-skill.md) |
| CRUD flows | [crud-flow-analysis-skill.md](crud-flow-analysis-skill.md) |
| Event flows | [event-flow-analysis-skill.md](event-flow-analysis-skill.md) |
| Integrations | [integration-flow-analysis-skill.md](integration-flow-analysis-skill.md) |
| Edx Builder | [edx-builder-understanding-skill.md](edx-builder-understanding-skill.md) |

## Do Not Proceed to Code When

- Target module is unknown
- Business rules contradict domain model
- No agreement on API operation list for new endpoints
