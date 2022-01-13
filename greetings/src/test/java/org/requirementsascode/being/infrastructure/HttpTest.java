package org.requirementsascode.being.infrastructure;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.requirementsascode.being.infrastructure.Bootstrap.CREATE_REQUEST;
import static org.requirementsascode.being.infrastructure.Bootstrap.FIND_ALL_REQUEST;
import static org.requirementsascode.being.infrastructure.Bootstrap.UPDATE_REQUEST;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

class HttpTest {
	private static final int PORT = 8081;
	private static final String DEFAULT_SALUTATION = "Hello,";

	@BeforeEach
	void setup() throws Exception {
		Bootstrap.main(new String[] {});
	}

	@Test
	void checkGreetingsCreationAndUpdate() {
		assertNoGreetingsCreated();
		
		final String jillsId = createAndAssertGreeting("Jill");
		final String joesId = createAndAssertGreeting("Joe");
		
		greetingsContain("id", jillsId, joesId);
		greetingsContain("personName", "Jill", "Joe");
		greetingsContain("salutation", DEFAULT_SALUTATION);
		
		updateAndAssertGreeting("Joe", joesId, "Hi,");
		updateAndAssertGreeting("Jill", jillsId, "Howdy");
		
		greetingsContain("id", jillsId, joesId);
		greetingsContain("personName", "Jill", "Joe");
		greetingsContain("salutation", "Howdy", "Hi,");
	}

	private void assertNoGreetingsCreated() {
		givenJsonClient()
			.get(FIND_ALL_REQUEST).then().body("$", hasSize(0));
	}
	
	private void greetingsContain(final String propertyName, final String... values) {
		givenJsonClient()
			.get(FIND_ALL_REQUEST).then().body(propertyName, hasItems(values));
	}

	private String createAndAssertGreeting(String personName) {
		Response response = 
			givenJsonClient()
				.body("{\"personName\":\"" + personName + "\"}")
				.post(CREATE_REQUEST);
			
		assertThat(json(response, "personName"), is(personName));
		assertThat(json(response, "salutation"), is(HttpTest.DEFAULT_SALUTATION));
		assertThat(json(response, "greetingText"), is("Hello, " + personName));
		return json(response, "id");
	}
	

	private void updateAndAssertGreeting(final String personName, final String greetingId, final String salutation) {
		Response response = 
			givenJsonClient()
				.body("{\"salutation\":\"" + salutation + "\"}")
				.patch(UPDATE_REQUEST.replace("{id}", greetingId));
				
			assertThat(json(response, "personName"), is(personName));
			assertThat(json(response, "salutation"), is(salutation));
			assertThat(json(response, "greetingText"), is(salutation + " " + personName));
	}

	private String json(Response response, String path) {
		return response.jsonPath().get(path);
	}

	protected RequestSpecification givenJsonClient() {
		return given()
			.port(PORT)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON);
	}
}
