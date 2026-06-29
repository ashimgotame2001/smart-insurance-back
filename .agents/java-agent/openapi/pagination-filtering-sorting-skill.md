# Pagination Filtering Sorting Skill

## Purpose

Standardize list API query parameters and response shape.

## Query Parameters

| Param | Type | Default | Description |
|-------|------|---------|-------------|
| `page` | integer | 0 | Zero-based page index |
| `size` | integer | 20 | Page size (max 100) |
| `sort` | string | module-defined | `field,asc` or `field,desc` |
| filter params | varies | — | e.g. `status`, `customerId`, `fromDate` |

## Response Shape

```yaml
PageResponse:
  properties:
    content:
      type: array
      items:
        $ref: '#/components/schemas/RemittanceSummary'
    page:
      type: integer
    size:
      type: integer
    totalElements:
      type: integer
    totalPages:
      type: integer
```

## Rules

- All list endpoints must paginate
- Document allowed sort fields in OpenAPI description
- Validate `size` max in controller or Platform resolver
- Use JPA `Pageable` in repository layer

See Platform pagination utilities in [../programming/platform-standard-usage-skill.md](../programming/platform-standard-usage-skill.md).
