# Domain Model Analysis Skill

## Purpose

Interpret ERDs and domain documentation to identify entities, aggregates, relationships, and repository boundaries.

## Analysis Steps

1. **Identify aggregates** — cluster entities with consistency boundaries
2. **Mark root entities** — entry point for transactions
3. **Define relationships** — OneToMany, ManyToOne, embedded value objects
4. **Extract invariants** — rules that must hold within aggregate
5. **Map to modules** — assign each aggregate to one SR product module

## Entity Checklist

| Aspect | Question |
|--------|----------|
| Identity | What is the primary key? `Long` vs business key? |
| Lifecycle | Created, updated, soft-deleted? |
| Audit | Who/when created and modified? |
| Status | State machine needed? |
| Ownership | Which aggregate owns this entity? |

## Output

- Entity list with attributes and types
- Relationship diagram (text or mermaid)
- Repository interface per aggregate root
- Cross-module references via ID only, not direct entity joins across modules

## Anti-Patterns

- Anemic domain with all logic in services when rules belong on entity
- Shared mutable entities across modules
- Database views exposed as JPA entities without justification
