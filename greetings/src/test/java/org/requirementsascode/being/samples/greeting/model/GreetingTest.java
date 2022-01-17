package org.requirementsascode.being.samples.greeting.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.requirementsascode.being.AggregateBehavior;
import org.requirementsascode.being.AggregateBehaviorTest;
import org.requirementsascode.being.samples.greeting.command.CreateGreeting;
import org.requirementsascode.being.samples.greeting.command.GreetingCommand;

class GreetingTest {
	private AggregateBehaviorTest<GreetingCommand, GreetingState> behaviorTest;

	@BeforeEach
	void setup() {
		this.behaviorTest = AggregateBehaviorTest.of(new Greeting());
	}

	@Test
	void createsOneGreeting() {
		behaviorTest.when(new CreateGreeting("Jack"));
		assertThat(behaviorTest.state().personName, is("Jack"));
	}
}
