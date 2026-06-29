# Product Layer Design Skill

## Purpose

Design reusable Smart Remittance capabilities in `sr-*` product modules.

## Product Module Contains

- Domain entities and core business rules
- Default service implementations
- Stable application interfaces for other modules
- Generic validators (not tenant-specific)
- OpenAPI-defined public APIs for the capability

## Product Module Must Not Contain

- Tenant-specific `if/else` branches
- Partner-specific API URLs or credentials
- Hardcoded solution configuration values

## Interface Design

- Expose interfaces for capabilities Solution layer may override
- Use DTOs at module boundaries
- Document extension points for Solution adapters

See [../core/platform-product-solution-boundary.md](../core/platform-product-solution-boundary.md).
