package org.requirementsascode.being.counter.concurrency;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.requirementsascode.being.counter.api.CounterService;

import akka.Done;

public class CounterServiceConcurrencyTest {
  @Test
  public void multithreadingCausesNoException() throws Exception {
    withServer(defaultSetup().withCassandra(), server -> {
      CounterService service = server.client(CounterService.class);
      
      final String aCounter = "aCounter";
      
      boolean pass = new CounterThreads(inc -> postCommand(service, aCounter, inc), 100).run();
      assertTrue(pass);
    });
  }

  private Done postCommand(CounterService service, String id, Object command){
    Done done = null;
    try {
      done = service.httpPost(id).invoke(command).toCompletableFuture().get(5, SECONDS);
    } catch (Exception e) {
      fail();
    } 
    return done;
  }
}
