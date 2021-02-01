package org.requirementsascode.being.counter.impl;

import lombok.Data;

@Data
class Counter{
  private final String id;
  private int value = 0;
  
  void increment() {
	  value++;
  }
}
