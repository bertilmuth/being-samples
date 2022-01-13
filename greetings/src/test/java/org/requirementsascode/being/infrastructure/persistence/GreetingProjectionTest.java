package org.requirementsascode.being.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.requirementsascode.being.ProjectionActor;
import org.requirementsascode.being.QueryModel;
import org.requirementsascode.being.QueryModelStateStoreProvider;
import org.requirementsascode.being.infrastructure.GreetingData;
import org.requirementsascode.being.model.greeting.GreetingCreated;
import org.requirementsascode.being.model.greeting.GreetingState;
import org.requirementsascode.being.model.greeting.SalutationChanged;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.Projection;
import io.vlingo.xoom.lattice.model.projection.TextProjectable;
import io.vlingo.xoom.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.xoom.symbio.BaseEntry;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.inmemory.InMemoryStateStoreActor;

public class GreetingProjectionTest {

	private World world;
	private StateStore stateStore;
	private Projection projection;
	private Map<String, String> valueToProjectionId;

	@BeforeEach
	public void setUp() {
		world = World.startWithDefaults("test-state-store-projection");
		NoOpDispatcher dispatcher = new NoOpDispatcher();
		valueToProjectionId = new ConcurrentHashMap<>();
		stateStore = world.actorFor(StateStore.class, InMemoryStateStoreActor.class,
				Collections.singletonList(dispatcher));
		StatefulTypeRegistry.registerAll(world, stateStore,GreetingData.class);

		QueryModel<GreetingData> queryModel = QueryModel.startEmpty(GreetingData.empty())
				.mergeEventsOf(GreetingCreated.class, (event,previousData) -> GreetingData.from(event.id, event.salutation, event.personName, null))
				.mergeEventsOf(SalutationChanged.class,
						(event,previousData) -> GreetingData.from(event.id, event.salutation, previousData.personName, previousData.greetingText));

		QueryModelStateStoreProvider.using(world.stage(), queryModel);

		projection = world.actorFor(Projection.class, ProjectionActor.class, stateStore, queryModel);
	}

	private void registerExampleGreeting(GreetingState firstData, GreetingState secondData) {
		final CountingProjectionControl control = new CountingProjectionControl();
		control.afterCompleting(2);
		projection.projectWith(createGreetingCreated(firstData), control);
		projection.projectWith(createGreetingCreated(secondData), control);
	}

	@Test
	public void ChangeGreetingText() {
		final GreetingData firstData = GreetingData.from("1", "", "first-greeting-text", "");
		final GreetingData secondData = GreetingData.from("2", "", "second-greeting-text", "");
		registerExampleGreeting(firstData.toGreetingState(), secondData.toGreetingState());

		final CountingProjectionControl control = new CountingProjectionControl();
		final AccessSafely access = control.afterCompleting(1);
		projection.projectWith(createGreetingTextChanged(firstData.toGreetingState()), control);
		final Map<String, Integer> confirmations = access.readFrom("confirmations");

		assertEquals(1, confirmations.size());
		assertEquals(1, valueOfProjectionIdFor(firstData.id, confirmations));

		CountingReadResultInterest interest = new CountingReadResultInterest();
		AccessSafely interestAccess = interest.afterCompleting(1);
		stateStore.read(firstData.id, GreetingData.class, interest);
		GreetingData item = interestAccess.readFrom("item", firstData.id);

		assertEquals(item.id, "1");
		assertEquals(item.personName, "first-greeting-text");
	}

	@Test
	public void CreateGreeting() {
		final GreetingData firstData = GreetingData.from("1", "", "first-greeting-text", "");
		final GreetingData secondData = GreetingData.from("2", "", "second-greeting-text", "");

		final CountingProjectionControl control = new CountingProjectionControl();
		final AccessSafely access = control.afterCompleting(2);
		projection.projectWith(createGreetingCreated(firstData.toGreetingState()), control);
		projection.projectWith(createGreetingCreated(secondData.toGreetingState()), control);
		final Map<String, Integer> confirmations = access.readFrom("confirmations");

		assertEquals(2, confirmations.size());
		assertEquals(1, valueOfProjectionIdFor(firstData.id, confirmations));
		assertEquals(1, valueOfProjectionIdFor(secondData.id, confirmations));

		CountingReadResultInterest interest = new CountingReadResultInterest();
		AccessSafely interestAccess = interest.afterCompleting(1);
		stateStore.read(firstData.id, GreetingData.class, interest);
		GreetingData item = interestAccess.readFrom("item", firstData.id);

		assertEquals(item.id, "1");
		assertEquals(item.personName, "first-greeting-text");

		interest = new CountingReadResultInterest();
		interestAccess = interest.afterCompleting(1);
		stateStore.read(secondData.id, GreetingData.class, interest);
		item = interestAccess.readFrom("item", secondData.id);
		assertEquals(secondData.id, item.id);
		assertEquals(item.id, "2");
		assertEquals(item.personName, "second-greeting-text");
	}

	private int valueOfProjectionIdFor(final String valueText, final Map<String, Integer> confirmations) {
		return confirmations.get(valueToProjectionId.get(valueText));
	}

	private Projectable createGreetingTextChanged(GreetingState data) {
		final SalutationChanged eventData = new SalutationChanged(data.id, data.salutation);

		BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(SalutationChanged.class, 1,
				JsonSerialization.serialized(eventData), 2, Metadata.withObject(eventData));

		final String projectionId = UUID.randomUUID().toString();
		valueToProjectionId.put(data.id, projectionId);

		return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
	}

	private Projectable createGreetingCreated(GreetingState data) {
		final GreetingCreated eventData = new GreetingCreated(data.id, data.salutation, data.personName);

		BaseEntry.TextEntry textEntry = new BaseEntry.TextEntry(GreetingCreated.class, 1,
				JsonSerialization.serialized(eventData), 1, Metadata.withObject(eventData));

		final String projectionId = UUID.randomUUID().toString();
		valueToProjectionId.put(data.id, projectionId);

		return new TextProjectable(null, Collections.singletonList(textEntry), projectionId);
	}

}
