[![Build Status](https://travis-ci.com/bertilmuth/being-samples.svg?branch=main)](https://travis-ci.com/bertilmuth/being-samples)
[![Gitter](https://badges.gitter.im/requirementsascode/community.svg)](https://gitter.im/requirementsascode/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

Counter sample project for the [Being](https://github.com/bertilmuth/being) project.

You can increment any number of integer counters by using POST requests to the service,
and get the current count using a GET request.

This sample also demonstrates how to use mutable state in the aggregate root.

# Getting started
You need to have Java 8 and Maven >= 3.6.0 or higher installed to run the samples. 

For more details, see [this page](https://www.lagomframework.com/documentation/1.6.x/java/JavaPrereqs.html#JDK).

In a terminal, switch to the main directory and run the service:

    cd counter
    mvn lagom:runAll

Press enter to stop the service.

# Usage
To get the current value of a counter, send a GET request to the [address](https://github.com/bertilmuth/being-samples/blob/main/counter/counter-api/src/main/java/org/requirementsascode/being/counter/api/CounterService.java) defined in the service interface.

Unix: `curl http://localhost:9000/api/counter/aCounter`

Windows (PowerShell): `iwr http://localhost:9000/api/counter/aCounter`

The service implementation answers the GET request with the response
defined by the [aggregate behavior](https://github.com/bertilmuth/being-samples/blob/main/counter/counter-impl/src/main/java/org/requirementsascode/being/counter/impl/CounterBehavior.java)'s `responseMessage()` method. In this sample, it just returns the counter's value.
Instead of `aCounter`, you can use any valid string as a counter identifier.
Each counter manages its (aggregate) state, i.e. the counter value, separately.

Use POST requests for commands. The JSON must contain a `@type` property with simple class name of command, e.g. `IncrementCounter`
to increase the counter value.

Example:

Unix: `curl -H "Content-Type: application/json" -X POST -d '{"@type": "IncrementCounter"}' http://localhost:9000/api/counter/aCounter`

Windows (PowerShell): `iwr http://localhost:9000/api/counter/aCounter -Method 'POST' -Headers @{'Content-Type' = 'application/json'} -Body '{"@type": "IncrementCounter"}'`

Now issue a GET request again, as shown above. As you'll see, the counter value has increased.
