[![Gitter](https://badges.gitter.im/requirementsascode/community.svg)](https://gitter.im/requirementsascode/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

You need to have Java 8 installed to run this sample. 

Follow these steps:

1. Open a shell, and clone the samples project:
`git clone https://github.com/bertilmuth/being-samples.git`

2. Switch to the sub directory of the sample:
`cd being-samples/greetings`

3. Run the server:
`./gradlew run`

4. Open a second shell. Now you can start sending requests.

## Send requests
In POST requests (for creating) and PATCH requests (for updating), use JSON to represent the commands.

Here are some example commands, together with example reponses from the server.

NOTE: On Windows 10 systems, use `curl.exe` instead of `curl`.

Create a greeting for Joe:

`curl -i -X POST -H "Content-Type: application/json" -d '{"personName":"Joe"}' http://localhost:8081/greetings`

Example response: 

``` shell
HTTP/1.1 201 Created
Content-Type: application/json; charset=UTF-8
Content-Length: 92

{"id":"898954e3-a886-4352-9283-320fc3a66c09","personName":"Joe","greetingText":"Hello, Joe"}
```

Afterwards, get Joe's greeting: 

`curl http://localhost:8081/greetings/898954e3-a886-4352-9283-320fc3a66c09`

Example response: 

`{"id":"898954e3-a886-4352-9283-320fc3a66c09","personName":"Joe","greetingText":"Hello, Joe"}`

Change Joe's greeting:

`curl -i -X PATCH -H "Content-Type: application/json" -d '{"salutation":"Howdy"}' http://localhost:8081/greetings/change/898954e3-a886-4352-9283-320fc3a66c09`

Example response: 

``` shell
HTTP/1.1 200 OK
Content-Type: application/json; charset=UTF-8
Content-Length: 91

{"id":"898954e3-a886-4352-9283-320fc3a66c09","personName":"Joe","greetingText":"Howdy Joe"}
```

Create a greeting for Jill: 

`curl -i -X POST -H "Content-Type: application/json" -d '{"personName":"Jill"}' http://localhost:8081/greetings`

Get all greetings: 

`curl.exe http://localhost:8081/greetings`

Example response: 

`[{"id":"898954e3-a886-4352-9283-320fc3a66c09","personName":"Joe","greetingText":"Howdy Joe"},{"id":"c37bfde8-4247-4c63-8607-d0453182859f","personName":"Jill","greetingText":"Hello, Jill"}]`