# Threat Intelligence

This repository has started as an exercise in demonstrating the building/organization of (initially) microservices related to the domain of Thread Intelligence in Cybersecurity.  

## Goals

* Demonstrate building a simple service for accessing data using Clojure
* Leverage Polylith (see details below)
* Use as a basis for ongoing exploration of techniques using Clojure, Polylith, and software design

## Future Items

With more time, I'm considering looking at some of the following goals

* Leverage *all* of the data in the seed file (e.g. author, tags, etc)
* More testing!
* Naming (are we ever happy with naming?).  Having more experience, especially with Polylith, I'd like to adjust the naming of some things
* Improve the indicator search capability (allowing multiple criteria per field, options to AND/OR, and include more fields from the source data)
* Continue refactoring to see if there are bits of the rest-api (such as Pedestal) that can be extracted to Polylith components cleanly and better leverage Clojure's Component library across Polylith components
* Add a new component for the indicator lookup/search that uses a database component as a backend (seeded by the source data). Might consider Sqlite, Datalevin, XTDB, or something similar. 
* Add an alternative logging component that uses another logger more condusive to observability
* Explore better integration or use of the logging component by Pedestal
* Experiment with how an upgrade of something significant (like new major version of Pedestal) would be supported by Polylith
* Add to the development project to make interactive coding via the REPL/editor on components/bases easier 
* Add a component for managing configuration

## Structured Using Polylith

<img src="logo.png" width="30%" alt="Polylith" id="logo">

The Polylith documentation can be found here:

- The [high-level documentation](https://polylith.gitbook.io/polylith)
- The [poly tool documentation](https://cljdoc.org/d/polylith/clj-poly/CURRENT)
- The [RealWorld example app documentation](https://github.com/furkan3ayraktar/clojure-polylith-realworld-example-app)

You can also get in touch with the Polylith Team on [Slack](https://clojurians.slack.com/archives/C013B7MQHJQ).

## Design/Architecture Decisions

This project keeps [Architecture Decision Records](https://adr.github.io) to document why many decisions were made. View them in the [`docs/decisions`](docs/decisions/) directory.

There were also a number of resources used in building this for reference and ideas.  See [references](/references.md)

## Building/Running

To build the indicators service, you can build an uberjar using the following script:

`./build-indicators-service`

Then, to run the service from the built uberjar, use the run script:

`./run-indicators-service`

If you would like to build a Docker container, once an uberjar is built, use:

`projects/indicators-service/build-docker-container`

And for convenience, you can run the docker container with this

`projects/indicators-service/run-docker-container`

By default, the service runs on port 8890 and listens on all interfaces for convenience running inside a Docker container.

## Trying it out

You can fetch all indicators using  a GET request to `/indicators`.  For example:

### Listing Indicators
```
curl http://localhost:8890/indicators
```

You can also filter by type with a query string parameter, such as `/indicators?type=IPv4`

```
curl http://localhost:8890/indicators\?type\=IPv4
```

### Lookup by ID

You can look up a specific indicator using `/indicators/:id`

```
curl http://localhost:8890/indicators/1169441112
```

### Search with criteria

So far, a simple form of search criteria is accepted in the form of a json object with fields/values to use as criteria, with the following current limitations:

* Only a single value per field is supported
* Field criteria are ORed, with no way yet to support an AND operation
* Some of the general metadata for specific indicators is not yet supported. You can only query against the fields you see in the indicator list.

```
~ curl \
  -H "Content-Type: application/json" \
  --request POST \
  --data '{"id": 1169441113}' \
  http://localhost:8890/indicators/search
```
