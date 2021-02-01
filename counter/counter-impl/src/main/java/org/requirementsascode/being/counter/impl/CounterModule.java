package org.requirementsascode.being.counter.impl;

import org.requirementsascode.being.AggregateModule;
import org.requirementsascode.being.counter.api.CounterService;

public class CounterModule extends AggregateModule {
  @Override
  protected void configure() {
    bindService(CounterService.class, CounterServiceImpl.class);
  }
}
