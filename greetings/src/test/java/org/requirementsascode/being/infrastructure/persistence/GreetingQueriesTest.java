package org.requirementsascode.being.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.requirementsascode.being.Queries;
import org.requirementsascode.being.QueriesActor;
import org.requirementsascode.being.infrastructure.GreetingData;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.common.Outcome;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.Result;
import io.vlingo.xoom.symbio.store.StorageException;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;

public class GreetingQueriesTest {

	private World world;
	private StateStore stateStore;
	private Queries<GreetingData> queries;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void setUp() {
		world = World.startWithDefaults("test-state-store-query");
		stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class,
				Collections.singletonList(new NoOpDispatcher()));
		StatefulTypeRegistry.registerAll(world, stateStore, GreetingData.class);
		queries = world.actorFor(Queries.class, QueriesActor.class, stateStore, GreetingData.class, GreetingData.empty());
	}

	private static final GreetingData FIRST_QUERY_BY_ID_TEST_DATA = GreetingData.from("1", "", "first-greeting-text", "");
	private static final GreetingData SECOND_QUERY_BY_ID_TEST_DATA = GreetingData.from("2", "", "second-greeting-text", "");

	@Test
	public void queryById() {
		stateStore.write("1", FIRST_QUERY_BY_ID_TEST_DATA, 1, NOOP_WRITER);
		stateStore.write("2", SECOND_QUERY_BY_ID_TEST_DATA, 1, NOOP_WRITER);

		final GreetingData firstData = queries.findById("1").await();

		assertEquals(firstData.id, "1");
		assertEquals(firstData.personName, "first-greeting-text");

		final GreetingData secondData = queries.findById("2").await();

		assertEquals(secondData.id, "2");
		assertEquals(secondData.personName, "second-greeting-text");
	}

	private static final GreetingData FIRST_QUERY_ALL_TEST_DATA = GreetingData.from("1", "", "first-greeting-text", "");
	private static final GreetingData SECOND_QUERY_ALL_TEST_DATA = GreetingData.from("2", "", "second-greeting-text", "");

	@Test
	public void queryAll() {
		stateStore.write("1", FIRST_QUERY_ALL_TEST_DATA, 1, NOOP_WRITER);
		stateStore.write("2", SECOND_QUERY_ALL_TEST_DATA, 1, NOOP_WRITER);

		final Collection<GreetingData> results = queries.findAll().await();
		final GreetingData firstData = results.stream().filter(data -> data.id.equals("1")).findFirst()
				.orElseThrow(RuntimeException::new);

		assertEquals(firstData.id, "1");
		assertEquals(firstData.personName, "first-greeting-text");

		final GreetingData secondData = results.stream().filter(data -> data.id.equals("2")).findFirst()
				.orElseThrow(RuntimeException::new);

		assertEquals(secondData.id, "2");
		assertEquals(secondData.personName, "second-greeting-text");
	}

	@Test
	public void greetingOfEmptyResult() {
		final GreetingData result = queries.findById("1").await();
		assertEquals("", result.id);
	}

	private static final StateStore.WriteResultInterest NOOP_WRITER = new StateStore.WriteResultInterest() {
		@Override
		public <S, C> void writeResultedIn(Outcome<StorageException, Result> outcome, String s, S s1, int i,
				List<Source<C>> list, Object o) {

		}
	};

}