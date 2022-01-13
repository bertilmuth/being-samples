package org.requirementsascode.being.infrastructure;

import org.requirementsascode.being.CommandModelJournalProvider;
import org.requirementsascode.being.HttpRequestHandlers;
import org.requirementsascode.being.ProjectionDispatcherProvider;
import org.requirementsascode.being.QueryModel;
import org.requirementsascode.being.QueryModelStateStoreProvider;
import org.requirementsascode.being.command.ChangeSalutation;
import org.requirementsascode.being.command.CreateGreeting;
import org.requirementsascode.being.command.GreetingCommand;
import org.requirementsascode.being.model.greeting.Greeting;
import org.requirementsascode.being.model.greeting.GreetingCreated;
import org.requirementsascode.being.model.greeting.GreetingState;
import org.requirementsascode.being.model.greeting.SalutationChanged;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.http.resource.Configuration.Sizing;
import io.vlingo.xoom.http.resource.Configuration.Timing;
import io.vlingo.xoom.http.resource.Resources;
import io.vlingo.xoom.http.resource.Server;
import io.vlingo.xoom.lattice.grid.Grid;
import io.vlingo.xoom.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.turbo.Boot;

@SuppressWarnings("all")
public class Bootstrap {
	public static final String CREATE_REQUEST = "/greetings";
	public static final String UPDATE_REQUEST = "/greetings/change/{id}";
	public static final String FIND_BY_ID_REQUEST = "/greetings/{id}";
	public static final String FIND_ALL_REQUEST = "/greetings";

	private final Grid grid;
	private final Server server;
	private static Bootstrap instance;

	public Bootstrap(final String nodeName) throws Exception {
		grid = Boot.start("being", nodeName);
		
		QueryModel<GreetingData> queryModel = 
			QueryModel.startEmpty(GreetingData.empty()) 
				.mergeEventsOf(GreetingCreated.class, (event,previousData) -> GreetingData.from(event.id, event.salutation, event.personName, ""))
				.mergeEventsOf(SalutationChanged.class, (event,previousData) -> GreetingData.from(event.id, event.salutation, previousData.personName, ""));
		
		startJournals(grid, queryModel);
		
		HttpRequestHandlers<GreetingCommand, GreetingState, GreetingData> greetingRequestHandlers = 
			HttpRequestHandlers.builder()
				.stage(grid)
				.aggregateSupplier(() -> new Greeting())
				.dataFromState(GreetingData::fromState)
				.createRequest(CREATE_REQUEST, CreateGreeting.class)
				.updateRequest(UPDATE_REQUEST, ChangeSalutation.class)
				.findByIdRequest(FIND_BY_ID_REQUEST)
				.findAllRequest(FIND_ALL_REQUEST)
				.build();

		Resources allResources = Resources.are(greetingRequestHandlers.routes());

		server = Server.startWith(grid.world().stage(), allResources, Boot.serverPort(), Sizing.define(),
				Timing.define());

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (instance != null) {
				instance.server.stop();

				System.out.println("\n");
				System.out.println("=========================");
				System.out.println("Stopping being.");
				System.out.println("=========================");
			}
		}));
	}

	private void startJournals(Stage stage, QueryModel<GreetingData> queryModel) {
		final Stage defaultStage = stage.world().stage();
		QueryModelStateStoreProvider.using(defaultStage, queryModel);

		Dispatcher dispatcher = ProjectionDispatcherProvider.using(defaultStage, queryModel).storeDispatcher;
		final SourcedTypeRegistry sourcedTypeRegistry = new SourcedTypeRegistry(stage.world());
		CommandModelJournalProvider.using(defaultStage, sourcedTypeRegistry, dispatcher);
	}

	public void stopServer() {
		if (instance == null) {
			throw new IllegalStateException("being server not running");
		}
		instance.server.stop();

		instance = null;
	}

	public Grid grid() {
		return grid;
	}

	public static void main(final String[] args) throws Exception {
		System.out.println("=========================");
		System.out.println("service: being.");
		System.out.println("=========================");

		instance = new Bootstrap(parseNodeName(args));
	}

	private static String parseNodeName(final String[] args) {
		if (args.length == 0) {
			return null;
		} else if (args.length > 1) {
			System.out.println("Too many arguments; provide node name only.");
			System.exit(1);
		}
		return args[0];
	}
}