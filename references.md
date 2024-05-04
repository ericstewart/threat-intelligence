# References Used

- Read through the documentation on [Polylith](https://polylith.gitbook.io/polylith) in general and the [clj-poly](https://github.com/polyfy/polylith) tool.  I also took a look at the [example systems](https://cljdoc.org/d/polylith/clj-poly/0.2.19/doc/example-systems) linked to by the `clj-poly` tool to confirm my understanding of the concepts.
- Looked up documentation for library: [clojure.json](https://github.com/clojure/data.json)
- Used the technique for redefining functions on component interfaces for testing from the [clojure-polylith-realworld-example-app](https://github.com/furkan3ayraktar/clojure-polylith-realworld-example-app/blob/master/bases/rest-api/test/clojure/realworld/rest_api/handler_test.clj)
- Referenced the Pedestal Guide on [Hello World, With Content Types](http://pedestal.io/pedestal/0.7/guides/hello-world-content-types.html), borrowing the implementation of a few route/interceptor functions to put some basic content negotiation and body coercion in place.
- Reviewed [Clojure Docker image page](https://hub.docker.com/_/clojure/) for options and example
- Copied the build configuration (tools.build) from the [clj-polylith project](https://github.com/polyfy/polylith/blob/v0.2.19/examples/doc-example/build.clj)
