package org.requirementsascode.being.counter.api;

import static java.util.Arrays.asList;

import java.util.List;

import org.requirementsascode.being.AggregateService;

public interface CounterService extends AggregateService {  
  @Override
  default String uniqueName() {
    return "GreetUserService";
  }
  
  @Override
  default String address() {
    return "/api/greet/:id";
  }
  
  @Override
  default List<Class<?>> incomingMessageTypes() {
    return asList(IncrementCounter.class);
  }
  
  @Override
  default List<Class<?>> outgoingMessageTypes() {
    return asList(CounterResponse.class);
  }
}
