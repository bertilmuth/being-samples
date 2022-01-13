package org.requirementsascode.being.infrastructure;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

class HttpTest {
	private static final int PORT = 8081;

	@BeforeEach
	void setup() throws Exception {
		Bootstrap.main(new String[] {});
	}

	@Test
	void noGreetingsAtFirst() {
		givenJsonClient().get("/greetings").then().body("$", hasSize(0));
	}

	protected RequestSpecification givenJsonClient() {
		return given()
			.port(PORT)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON);
	}
}
