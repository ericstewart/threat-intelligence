# Use Pedestal for serving the API

- Status: accepted
- Date: 2024-04-29

## Context and Problem Statement

We need a way to run a microservice service RESTful endpoint for interacting with indicators.

## Decision Drivers <!-- optional -->

- The source outline for this project indicated Pedestal, although when using Polylith, Pedestal could be replaced with Ring. 
- Polylith was already chosen

## Considered Options

- Pedestal 0.6 (Current stable version)
- Pedestal 0.7 (just reached beta at the time of this decision)
- Ring

## Decision Outcome

Chosen option: "Pedestal 0.6", because it is the requested default by the project, I had some familiarity, and the version is the stable release version.  While Pedestal 0.7 appears to have some interesting improvements, none seem necessary for this phase of the project.

