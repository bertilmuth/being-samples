package org.requirementsascode.being.greetuser.api;

import org.requirementsascode.being.*;

import lombok.Value;

@Value @Properties
public class GreetingResponse{
  String text;
}