package org.requirementsascode.being.counter.impl;

import org.requirementsascode.being.AggregateBehaviorTest;
import org.requirementsascode.being.counter.api.IncrementCounter;

public class CounterThreads {
  private int threadCount;
  private AggregateBehaviorTest<Counter> behaviorTest;

  public CounterThreads(AggregateBehaviorTest<Counter> behaviorTest, int threadCount) {
    this.behaviorTest = behaviorTest;
    this.threadCount = threadCount;
  }

  public boolean run() {
    boolean pass = true;
    
    CounterRunnable[] runnables = new CounterRunnable[threadCount];
    Thread[] threads = new Thread[threadCount];

    for (int i = 0; i < threadCount; i++) {
      runnables[i] = new CounterRunnable(behaviorTest);
      threads[i] = new Thread(runnables[i]);
      threads[i].start();
    }
    
    for (int i = 0; i < threadCount; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
      }
      
      if (!runnables[i].hasPassed()) {
        pass = false;
        break;
      }
    }
    
    return pass;
  }
}

class CounterRunnable implements Runnable{
  private final AggregateBehaviorTest<Counter> behaviorTest;
  boolean pass = true;

  public CounterRunnable(AggregateBehaviorTest<Counter> behaviorTest) {
    this.behaviorTest = behaviorTest;
  }
  
  public void run() {
    try {
      behaviorTest.when(new IncrementCounter());
    } catch(Exception e) {
      pass = false;
    }
  }
  
  public boolean hasPassed() {
    return pass;
  }
}
