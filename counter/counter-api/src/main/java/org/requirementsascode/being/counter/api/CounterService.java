package org.requirementsascode.being.counter.api;

import static java.util.Arrays.asList;

import java.util.List;

import org.requirementsascode.being.AggregateService;

public interface CounterService extends AggregateService {  
  @Override
  default String uniqueName() {
    return "CounterService";
  }
  
  @Override
  default String address() {
    return "/api/counter/:id";
  }
  
  @Override
  default List<Class<?>> commandTypes() {
    return asList(IncrementCounter.class);
  }
  
  @Override
  default List<Class<?>> responseTypes() {
    return asList(CounterResponse.class);
  }
}
