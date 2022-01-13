package org.requirementsascode.being.model.greeting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.requirementsascode.being.AggregateBehavior;
import org.requirementsascode.being.EventSourcedAggregate;
import org.requirementsascode.being.EventSourcedAggregateBehavior;
import org.requirementsascode.being.command.ChangeSalutation;
import org.requirementsascode.being.command.GreetingCommand;
import org.requirementsascode.being.infrastructure.persistence.MockDispatcher;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.xoom.symbio.BaseEntry;
import io.vlingo.xoom.symbio.store.journal.Journal;
import io.vlingo.xoom.symbio.store.journal.inmemory.InMemoryJournalActor;

public class GreetingAggregateTest {

	private World world;
	private MockDispatcher dispatcher;
	private AggregateBehavior<GreetingCommand, GreetingState> behavior;

	@BeforeEach
	public void setUp() {
		world = World.startWithDefaults("test-es");
		dispatcher = new MockDispatcher();

		this.behavior = createTestBehavior(world, dispatcher, () -> new Greeting(), "#1");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private AggregateBehavior<GreetingCommand, GreetingState> createTestBehavior(World world, MockDispatcher dispatcher,
			Supplier<EventSourcedAggregate<GreetingCommand, GreetingState>> aggregateSupplier, String aggregateId) {

		Journal<String> journal = world.actorFor(Journal.class, InMemoryJournalActor.class,
				Collections.singletonList(dispatcher));

		new SourcedTypeRegistry(world).register(new Info(journal, EventSourcedAggregateBehavior.class,
				EventSourcedAggregateBehavior.class.getSimpleName()));

		AggregateBehavior<GreetingCommand, GreetingState> behavior = world.actorFor(AggregateBehavior.class,
				EventSourcedAggregateBehavior.class, aggregateId, aggregateSupplier);
		return behavior;
	}

	private static final String TEXT_FOR_CHANGE_GREETING_SALUTATION_TEST = "greeting-text";

	@SuppressWarnings("unchecked")
	@Test
	public void ChangeGreetingText() {
		final AccessSafely dispatcherAccess = dispatcher.afterCompleting(1);
		final ChangeSalutation command = new ChangeSalutation(TEXT_FOR_CHANGE_GREETING_SALUTATION_TEST);
		final GreetingState state = (GreetingState) behavior.reactTo(command).await();

		assertEquals(TEXT_FOR_CHANGE_GREETING_SALUTATION_TEST, state.salutation);
		assertEquals(1, (int) dispatcherAccess.readFrom("entriesCount"));
		assertEquals(SalutationChanged.class.getName(),
				((BaseEntry<String>) dispatcherAccess.readFrom("appendedAt", 0)).typeName());
	}
}
