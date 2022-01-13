package org.requirementsascode.being.infrastructure;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.requirementsascode.being.infrastructure.Bootstrap.*;
import static org.requirementsascode.being.infrastructure.Bootstrap.FIND_ALL_PATH;
import static org.requirementsascode.being.infrastructure.Bootstrap.UPDATE_PATH;

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
			.get(FIND_ALL_PATH)
			.then()
			.body("$", hasSize(0));
	}
	
	private void greetingsContain(final String propertyName, final String... values) {
		givenJsonClient()
			.get(FIND_ALL_PATH)
			.then()
			.body(propertyName, hasItems(values));
	}

	private String createAndAssertGreeting(String personName) {
		Response greetingData = 
			givenJsonClient()
				.body("{\"personName\":\"" + personName + "\"}")
				.post(CREATE_PATH);
			
		assertThat(json(greetingData, "personName"), is(personName));
		assertThat(json(greetingData, "salutation"), is(DEFAULT_SALUTATION));
		assertThat(json(greetingData, "greetingText"), is("Hello, " + personName));
		
		final String greetingId = json(greetingData, "id");
		
		givenJsonClient()
			.get(pathWithId(FIND_BY_ID_PATH, greetingId))
			.then()
			.body("id", is(greetingId))
			.body("salutation", is(DEFAULT_SALUTATION));
				
		return greetingId;
	}
	

	private void updateAndAssertGreeting(final String personName, final String greetingId, final String salutation) {
		Response greetingData = 
			givenJsonClient()
				.body("{\"salutation\":\"" + salutation + "\"}")
				.patch(pathWithId(UPDATE_PATH, greetingId));
				
		assertThat(json(greetingData, "personName"), is(personName));
		assertThat(json(greetingData, "salutation"), is(salutation));
		assertThat(json(greetingData, "greetingText"), is(salutation + " " + personName));
		
		givenJsonClient()
			.get(pathWithId(FIND_BY_ID_PATH, greetingId))
			.then()
			.body("id", is(greetingId))
			.body("salutation", is(salutation));
	}

	private String pathWithId(final String path, final String greetingId) {
		return path.replace("{id}", greetingId);
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
