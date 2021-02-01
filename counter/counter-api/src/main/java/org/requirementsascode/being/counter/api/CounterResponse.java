package org.requirementsascode.being.counter.api;

import org.requirementsascode.being.Properties;

import lombok.Value;

@Value @Properties
public class CounterResponse{
  int counterValue;
}