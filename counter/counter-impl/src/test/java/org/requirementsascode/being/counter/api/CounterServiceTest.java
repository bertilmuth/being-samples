package org.requirementsascode.being.counter.api;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.requirementsascode.being.JsonMessage;
import org.requirementsascode.being.counter.api.CounterResponse;
import org.requirementsascode.being.counter.api.CounterService;
import org.requirementsascode.being.counter.api.IncrementCounter;

import akka.Done;

public class CounterServiceTest {
  @Test
  public void storesPersonalizedGreeting() throws Exception {
    withServer(defaultSetup().withCassandra(), server -> {
      CounterService service = server.client(CounterService.class);
      
      final String aCounter = "aCounter";
      CounterResponse response = get(service, aCounter);
      assertEquals(0, response.getCounterValue()); // initial count is 0
      
      postIncrementCounter(service, aCounter);
      CounterResponse response2 = get(service, aCounter);
      assertEquals(1, response2.getCounterValue());
    });
  }

  private CounterResponse get(CounterService service, String id)
      throws InterruptedException, ExecutionException, TimeoutException {
    JsonMessage message = service.httpGet(id).invoke().toCompletableFuture().get(5, SECONDS);
    CounterResponse response = (CounterResponse)message.payload();
    return response;
  }

  private Done postIncrementCounter(CounterService service, String id) throws Exception {
    IncrementCounter command = new IncrementCounter();
    Done done = service.httpPost(id).invoke(command).toCompletableFuture().get(5, SECONDS);
    return done;
  }
}
