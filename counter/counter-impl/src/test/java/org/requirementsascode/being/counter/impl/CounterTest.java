package org.requirementsascode.being.counter.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.requirementsascode.being.AggregateBehaviorTest;
import org.requirementsascode.being.counter.api.IncrementCounter;

public class CounterTest {

  @Test
  public void initialCounterIsZero() {
    CounterBehavior behavior = new CounterBehavior();
    AggregateBehaviorTest<Counter> behaviorTest = AggregateBehaviorTest.of(behavior);

    behaviorTest.givenEvents();
    assertEquals(0, behavior.aggregateRoot().getValue());
  }

  @Test
  public void incrementsCounterOnce() {
    CounterBehavior behavior = new CounterBehavior();
    AggregateBehaviorTest<Counter> behaviorTest = AggregateBehaviorTest.of(behavior);

    behaviorTest.when(new IncrementCounter());
    assertEquals(1, behavior.aggregateRoot().getValue());
  }

  @Test
  public void incrementsCounterTwice() {
    CounterBehavior behavior = new CounterBehavior();
    AggregateBehaviorTest<Counter> behaviorTest = AggregateBehaviorTest.of(behavior);

    behaviorTest
      .givenEvents(new CounterBehavior.CounterIncremented())
      .when(new IncrementCounter());

    assertEquals(2, behavior.aggregateRoot().getValue());
  }
}
