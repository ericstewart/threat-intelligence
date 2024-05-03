# Threat Intelligence

This repository has started as an exercise in demonstrating the building/organization of (initially) microservices related to the domain of Thread Intelligence in Cybersecurity.  

The implemenation technology stack is Clojure.


## Structured Using Polylith

<img src="logo.png" width="30%" alt="Polylith" id="logo">

The Polylith documentation can be found here:

- The [high-level documentation](https://polylith.gitbook.io/polylith)
- The [poly tool documentation](https://cljdoc.org/d/polylith/clj-poly/CURRENT)
- The [RealWorld example app documentation](https://github.com/furkan3ayraktar/clojure-polylith-realworld-example-app)

You can also get in touch with the Polylith Team on [Slack](https://clojurians.slack.com/archives/C013B7MQHJQ).

## Design/Architecture Decisions

This project keeps [Architecture Decision Records](https://adr.github.io) to document why many decisions were made. View them in the [`docs/decisions`](docs/decisions/) directory.

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
