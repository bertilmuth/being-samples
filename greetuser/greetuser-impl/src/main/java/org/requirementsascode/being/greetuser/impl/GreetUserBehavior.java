package org.requirementsascode.being.greetuser.impl;

import org.requirementsascode.Model;
import org.requirementsascode.being.AggregateBehavior;
import org.requirementsascode.being.Properties;
import org.requirementsascode.being.greetuser.api.ChangeGreetingText;
import org.requirementsascode.being.greetuser.api.GreetingResponse;

import lombok.Value;

class GreetUserBehavior extends AggregateBehavior<Greeting>{
  @Override
  public Greeting createAggregateRoot(String aggregateId) {
    return Greeting.create(aggregateId, "Hello");
  }
  
  @Override
  public Object responseMessage() {
    return new GreetingResponse(aggregateRoot().getText() + ", " + aggregateRoot().getId() + "!");
  }
  
  @Override
  public Model incomingMessageHandlers() {
    Model model = Model.builder()
      .user(ChangeGreetingText.class).systemPublish(this::greetingTextChanged)
      .build();
    return model;
  }
  
  @Override
  public Model internalEventHandlers() {
    Model model = Model.builder()
      .on(GreetingTextChanged.class).systemPublish(this::newGreeting)
      .build();
    return model;
  }
  
  private GreetingTextChanged greetingTextChanged(ChangeGreetingText command) {
    String newText = command.getNewText();
    if(newText.isEmpty()) {
      throw new RuntimeException("Text must not be empty!");
    }
    
    GreetingTextChanged event = new GreetingTextChanged(newText);
    return event;
  }
  
  private Greeting newGreeting(GreetingTextChanged event) {
    return Greeting.create(aggregateRoot().getId(), event.getText()); 
  }
  
  // Internal event classes
  
  @Value @Properties
  static final class GreetingTextChanged{
    String text;
  }
}