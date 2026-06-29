# Code Review Checklist

- [ ] Industry standards: SOLID layering, constructor injection, no entity leakage — see [industry-coding-standards-skill.md](../programming/industry-coding-standards-skill.md)
- [ ] Correct design pattern(s) by category (Creational / Structural / Behavioral) for feature type — see [design-pattern-selection-skill.md](../programming/design-pattern-selection-skill.md)
- [ ] Layering: controller → service → repository
- [ ] No entities in API responses
- [ ] DTO validation present
- [ ] Business rules in service/domain, not controller
- [ ] Platform utilities reused
- [ ] No tenant logic in Product module
- [ ] Exceptions use Platform types and stable codes
- [ ] `@Transactional` on service writes only
- [ ] Tests cover happy path and key errors
- [ ] List APIs use projections where appropriate
- [ ] Traceability IDs in JavaDoc/OpenAPI

See [../review/review-skill.md](../review/review-skill.md).
