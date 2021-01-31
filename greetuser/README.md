Sample project for the [being](https://github.com/bertilmuth/being) project.

# Getting started
You need to have Java 8 and Maven >= 3.6.0 or higher installed to run the samples. 

For more details, see [this page](https://www.lagomframework.com/documentation/1.6.x/java/JavaPrereqs.html#JDK).

In a terminal, switch to the main directory and run the service:

    cd greetuser
    mvn lagom:runAll

Press enter to stop the service.

# Usage
Example URL for GET/POST requests: 

http://localhost:9000/api/greet/Joe

If you send a GET request to the above URL, Joe will get greeted.
Replace Joe by a different name, and the greeting will change.
Internally, the service implementation responds to GET requests with the response
defined by the [aggregate behavior](https://github.com/bertilmuth/being-samples/blob/main/greetuser/greetuser-impl/src/main/java/org/requirementsascode/being/greetuser/impl/GreetUserBehavior.java)'s `responseMessage()` method.

Use POST requests for commands. The JSON must contain a `@type` property with simple class name of command, e.g. `ChangeGreetingText`.
Example JSON:

`{"@type":"ChangeGreetingText", "newText":"Guten Tag"}`
