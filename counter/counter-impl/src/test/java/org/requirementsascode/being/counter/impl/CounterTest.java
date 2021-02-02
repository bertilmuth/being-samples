package org.requirementsascode.being.counter.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.requirementsascode.being.AggregateBehaviorTest;
import org.requirementsascode.being.counter.api.IncrementCounter;

public class CounterTest {
  private CounterBehavior behavior;
  private AggregateBehaviorTest<Counter> behaviorTest;

  @Before
  public void setup() {
    behavior = new CounterBehavior();
    behaviorTest = AggregateBehaviorTest.of(behavior);
  }

  @Test
  public void initialCounterIsZero() {
    assertEquals(0, behavior.aggregateRoot().getValue());
  }

  @Test
  public void incrementsCounterOnce() {
    behaviorTest.when(new IncrementCounter());
    assertEquals(1, behavior.aggregateRoot().getValue());
  }

  @Test
  public void incrementsCounterTwice() {
    behaviorTest.givenEvents(new CounterBehavior.CounterIncremented()).when(new IncrementCounter());

    assertEquals(2, behavior.aggregateRoot().getValue());
  }
}
