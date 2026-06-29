# Edx Builder Understanding Skill

## Purpose

Interpret Edx Builder generated or configured artifacts (screens, forms, workflows) and align them with backend module structure.

## Edx Builder Outputs to Map

| Builder artifact | Backend mapping |
|------------------|-----------------|
| Form definition | Request DTO + validation annotations |
| List configuration | GET endpoint + query params |
| Workflow step | Service orchestration method |
| Business rule config | Validator or domain rule (Product vs Solution) |
| Module reference | Target `sr-*` Gradle module |

## Analysis Steps

1. Identify Builder module and version context
2. Extract field metadata (type, required, regex, lookup)
3. Map lookup fields to reference data APIs
4. Confirm generated UI matches functional prototype
5. Flag Builder-only rules that need Solution-layer override

## Caution

- Builder config may encode Solution-specific behavior — verify layer placement
- Do not auto-generate entities from Builder alone; cross-check domain model
