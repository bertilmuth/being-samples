package org.requirementsascode.being.counter.impl;

import org.requirementsascode.being.Properties;

import lombok.Data;

@Data @Properties
class Counter {
  final String id;
  int value;

  void increment() {
    value++;
  }
}
