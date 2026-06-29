# Workflow Analysis Skill

## Purpose

Analyze multi-step business processes (remittance send, KYC approval, payout settlement) and map them to service orchestration and state transitions.

## Steps

1. Document happy path and alternate paths
2. Identify decision points and guards
3. Map each step to a service method or domain event
4. Define entity status enum covering all states
5. Identify compensating actions for failures

## State Transition Template

| From State | Event / Action | To State | Side Effects |
|------------|----------------|----------|--------------|

## Orchestration Rules

- One aggregate owns the workflow state
- Cross-module steps call application services via adapters
- Long-running flows use events or saga pattern; avoid blocking chains
- Idempotency keys for retry-safe steps

## Output

Workflow diagram + service method list + event list
