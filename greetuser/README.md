Sample project for the [Being](https://github.com/bertilmuth/being) project.
This is a variation of the Lagom hello world example.

# Getting started
You need to have Java 8 and Maven >= 3.6.0 installed to run the samples. 

For more details on prerequisites, see [this page](https://www.lagomframework.com/documentation/1.6.x/java/JavaPrereqs.html#JDK).

In a terminal, switch to the main directory and run the service:

    cd greetuser
    mvn lagom:runAll

Press enter to stop the service.

# Usage
To receive a greeting, send a GET request to the [address](https://github.com/bertilmuth/being-samples/blob/main/greetuser/greetuser-api/src/main/java/org/requirementsascode/being/greetuser/api/GreetUserService.java) defined in the service interface.

Unix: `curl http://localhost:9000/api/greet/Joe`

Windows (PowerShell): `iwr http://localhost:9000/api/greet/Joe`

You'll receive a response with the text "Hello, Joe!". 
Replace Joe by a different name, and the greeting will change.
The service implementation answers the GET request with the response
defined by the [aggregate behavior](https://github.com/bertilmuth/being-samples/blob/main/greetuser/greetuser-impl/src/main/java/org/requirementsascode/being/greetuser/impl/GreetUserBehavior.java)'s `responseMessage()` method.

Use POST requests for commands. The JSON must contain a `@type` property with simple class name of command, e.g. `ChangeGreetingText`.

Example:

Unix: `curl -H "Content-Type: application/json" -X POST -d '{"@type": "ChangeGreetingText", "newText":"Hi"}' http://localhost:9000/api/greet/Joe`

Windows (PowerShell): `iwr http://localhost:9000/api/greet/Joe -Method 'POST' -Headers @{'Content-Type' = 'application/json'} -Body '{"@type": "ChangeGreetingText", "newText":"Hi"}'`

Now issue a GET request again, as shown above. As you'll see, the text has changed to "Hi, Joe!".
But if you send a GET request for a different name, it will still greet with "Hello".

That's because for every name, there is a `Greeting` aggregate with the name as the id,
that saves the events that occur for it separately.