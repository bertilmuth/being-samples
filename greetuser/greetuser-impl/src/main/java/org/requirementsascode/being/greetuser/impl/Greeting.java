package org.requirementsascode.being.greetuser.impl;

import java.time.LocalDateTime;

import org.requirementsascode.being.Properties;

import lombok.Value;

@Value @Properties
class Greeting{
  String id;
  String text;
  String timestamp;

  public static Greeting create(String id, String text) {
    return new Greeting(id, text, LocalDateTime.now().toString());
  }
}
