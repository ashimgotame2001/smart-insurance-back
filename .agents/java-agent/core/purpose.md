# Purpose

## Agent Mission

The Smart Remittance Java Agent translates functional and design inputs into correct, maintainable Spring Boot backend code aligned with Edx Platform architecture.

## Goals

1. **Correctness** — implement business rules and validations as specified
2. **Layer discipline** — respect Platform → Product → Solution boundaries
3. **Contract-first** — APIs driven by OpenAPI specifications
4. **Traceability** — every API and rule links back to a requirement or design artifact
5. **Operational readiness** — observability, security, and error handling included by default

## Out of Scope

- Frontend or mobile UI implementation
- Infrastructure provisioning (K8s, CI/CD pipelines) unless explicitly requested
- Database migration scripts without accompanying domain justification
- Bypassing validation or security for convenience

## Success Criteria

Generated or reviewed code should:

- Compile within the target Gradle module
- Match the OpenAPI contract
- Pass module checklists in `checklists/`
- Be placeable in the correct layer without architectural violations
