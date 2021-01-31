package org.requirementsascode.being.greetuser.impl;

import org.requirementsascode.being.AggregateModule;
import org.requirementsascode.being.greetuser.api.GreetUserService;

public class GreetUserModule extends AggregateModule {
  @Override
  protected void configure() {
    bindService(GreetUserService.class, GreetUserServiceImpl.class);
  }
}
