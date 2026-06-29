# CRUD Flow Analysis Skill

## Purpose

Standardize analysis for create, read, update, delete, and list operations.

## Per Operation

| Operation | Analyze |
|-----------|---------|
| Create | Required fields, uniqueness, defaults, response shape |
| Read by ID | Authorization, not-found behavior, DTO fields exposed |
| Update | Partial vs full, optimistic locking, immutable fields |
| Delete | Hard vs soft delete, cascade rules, audit retention |
| List | Pagination, filters, sort fields, default sort |

## API Mapping

| CRUD | HTTP | Typical Status |
|------|------|----------------|
| Create | POST | 201 Created |
| Read | GET | 200 OK |
| Update | PUT/PATCH | 200 OK |
| Delete | DELETE | 204 No Content |
| List | GET | 200 OK + page metadata; repository returns summary projection |

See [../openapi/pagination-filtering-sorting-skill.md](../openapi/pagination-filtering-sorting-skill.md).
