package org.requirementsascode.being.counter.concurrency;

import java.util.function.Consumer;

import org.requirementsascode.being.counter.api.IncrementCounter;

class CounterThreads {
  private int threadCount;
  private Consumer<Object> behaviorTest;

  public CounterThreads(Consumer<Object> behaviorTest, int threadCount) {
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
        runnables[i].calculateElapsedTime(System.currentTimeMillis());
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
  private final Consumer<Object> behaviorTest;
  boolean pass = true;
  private long startTime;

  public CounterRunnable(Consumer<Object> behaviorTest2) {
    this.behaviorTest = behaviorTest2;
  }
  
  public void run() {
    startTime = System.currentTimeMillis();
    try {
      behaviorTest.accept(new IncrementCounter());
    } catch(Exception e) {
      pass = false;
    }
  }
  
  public boolean hasPassed() {
    return pass;
  }
  
  public long calculateElapsedTime(long endTimeInMillis) {
    long elapsedTime = endTimeInMillis - startTime;
    return elapsedTime;
  }
}
