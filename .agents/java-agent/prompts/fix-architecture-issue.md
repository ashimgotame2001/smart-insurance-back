# Fix Architecture Issue

Use the Smart Remittance Java Agent skill.

## Task

Fix an architecture violation (layer bleed, wrong module, tenant logic in Product, etc.).

## Inputs

- Issue description:
- Affected files:
- Expected layer/module placement:

## Instructions

1. Read [platform-product-solution-boundary.md](../core/platform-product-solution-boundary.md)
2. Diagnose violation type (layer, module, runtime switching)
3. Propose minimal refactor path
4. Move code with updated tests
5. Verify checklists pass after fix

## Output

Refactor plan + code changes + before/after placement table
