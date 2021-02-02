Sample project for the [Being](https://github.com/bertilmuth/being) project.

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
defined by the [aggregate behavior](https://github.com/bertilmuth/being-samples/blob/main/counter/counter-impl/src/main/java/org/requirementsascode/being/counter/impl/CounterBehavior.java)'s `responseMessage()` method.

Use POST requests for commands. The JSON must contain a `@type` property with simple class name of command, e.g. `IncrementCounter`.

Example:

Unix: `curl -H "Content-Type: application/json" -X POST -d '{"@type": "IncrementCounter"}' http://localhost:9000/api/counter/aCounter`

Windows (PowerShell): `iwr http://localhost:9000/api/counter/aCounter -Method 'POST' -Headers @{'Content-Type' = 'application/json'} -Body '{"@type": "IncrementCounter"}'`

Now issue a GET request again, as shown above. As you'll see, the text has changed to "Hi, Joe!".
But if you send a GET request for a different name, it will still greet with "Hello".

That's because for every name, there is a `Greeting` aggregate with the name as the id,
that saves the events that occur for it separately.
