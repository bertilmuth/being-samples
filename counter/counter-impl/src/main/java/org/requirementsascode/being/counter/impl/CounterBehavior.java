package org.requirementsascode.being.counter.impl;

import org.requirementsascode.Model;
import org.requirementsascode.being.AggregateBehavior;
import org.requirementsascode.being.Properties;
import org.requirementsascode.being.counter.api.CounterResponse;
import org.requirementsascode.being.counter.api.IncrementCounter;

class CounterBehavior extends AggregateBehavior<Counter>{
  @Override
  public Counter createAggregateRoot(String aggregateId) {
    return new Counter(aggregateId);
  }
  
  @Override
  public Object responseMessage() {
    return new CounterResponse(aggregateRoot().getValue());
  }
  
  @Override
  public Model incomingMessageHandlers() {
    Model model = Model.builder()
      .user(IncrementCounter.class).systemPublish(this::counterIncremented)
      .build();
    return model;
  }
  
  @Override
  public Model internalEventHandlers() {
    Model model = Model.builder()
      .on(CounterIncremented.class).system(this::incrementCounter)
      .build();
    return model;
  }
  
  private CounterIncremented counterIncremented(IncrementCounter command) {
    CounterIncremented event = new CounterIncremented();
    return event;
  }
  
  private void incrementCounter() {
	  aggregateRoot().increment();
  }
  
  // Internal event classes
  
  @Properties
  static final class CounterIncremented{
  }
}