package org.requirementsascode.being.counter.impl;

import org.requirementsascode.being.AggregateBehavior;
import org.requirementsascode.being.AggregateServiceImpl;
import org.requirementsascode.being.counter.api.CounterService;

class CounterServiceImpl extends AggregateServiceImpl<Counter> implements CounterService{
  @Override
  public Class<Counter> aggregateRootClass() {
    return Counter.class;
  }

  @Override
  public AggregateBehavior<Counter> aggregateBehavior() {
    return new CounterBehavior();
  }
}
