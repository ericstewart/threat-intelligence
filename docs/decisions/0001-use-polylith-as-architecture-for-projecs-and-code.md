# Use Polylith as Architecture for Projecs and Code

- Status: accepted
- Date: 2024-04-28

## Context and Problem Statement

We need a strategy for organization of projects/code. 

## Decision Drivers

- Every project/repo has to be structured *somehow*
- The source requirements for this project indicated bonus points for leveraging [Polylith](https://polylith.gitbook.io/polylith).
- Short-term nature of this project (trying something unfamiliar or too new may detract from the goal)

## Considered Options

- Monorepo with single-project organizational structure
- Custom organization 
- [Polylith](https://polylith.gitbook.io/polylith)

## Decision Outcome

Chosen option: "[Polylith]", since it was a nice to have and was already somewhat familiar due to prior investigation.

### Positive Consequences

- Demonstration of understanding
- Modular code organization, reusability, etc (the Polylith selling points)

### Negative Consequences

- May run into unnecessary blockers or bottlenecks that detract from the primary project goal
- Should this happen, need to re-evaluate and fall back to a simpler approach.

