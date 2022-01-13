package org.requirementsascode.being.command;

public class CreateGreeting implements GreetingCommand{
	public final String personName;

	public CreateGreeting(String personName) {
		this.personName = personName;
	}
	
	@Override
	public String toString() {
		return "CreateGreeting [personName=" + personName + "]";
	}
}