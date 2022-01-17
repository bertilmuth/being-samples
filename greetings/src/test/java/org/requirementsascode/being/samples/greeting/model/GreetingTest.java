package org.requirementsascode.being.samples.greeting.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.requirementsascode.being.AggregateBehaviorTest;
import org.requirementsascode.being.samples.greeting.command.ChangeSalutation;
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
	
	@Test
	void updatesGreetingOnce() {
		behaviorTest
			.givenEvents(new GreetingCreated("#1", "Hi", "Jill"))
			.when(new ChangeSalutation("Hello"));
		
		final GreetingState expectedState = new GreetingState("#1", "Hello", "Jill");
		assertThat(behaviorTest.state(), is(expectedState));
	}
	
	@Test
	void updatesGreetingTwice() {
		behaviorTest
			.givenEvents(
				new GreetingCreated("#1", "Hi", "Jill"),
				new SalutationChanged("#1", "Howdy"))
			.when(new ChangeSalutation("Moin"));
		
		final GreetingState expectedState = new GreetingState("#1", "Moin", "Jill");
		assertThat(behaviorTest.state(), is(expectedState));
	}
}
