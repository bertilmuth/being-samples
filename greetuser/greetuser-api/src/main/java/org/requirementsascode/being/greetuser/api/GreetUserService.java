package org.requirementsascode.being.greetuser.api;

import static java.util.Arrays.asList;

import java.util.List;

import org.requirementsascode.being.AggregateService;

public interface GreetUserService extends AggregateService {  
  @Override
  default String uniqueName() {
    return "GreetUserService";
  }
  
  @Override
  default String address() {
    return "/api/greet/:id";
  }
  
  @Override
  default List<Class<?>> commandTypes() {
    return asList(ChangeGreetingText.class);
  }
  
  @Override
  default List<Class<?>> responseTypes() {
    return asList(GreetingResponse.class);
  }
}
