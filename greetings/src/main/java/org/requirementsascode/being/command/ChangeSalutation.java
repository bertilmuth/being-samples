package org.requirementsascode.being.command;

public class ChangeSalutation implements GreetingCommand{
	public final String salutation;

	public ChangeSalutation(final String salutation) {
		this.salutation = salutation;
	}

	@Override
	public String toString() {
		return "ChangeSalutation [salutation=" + salutation + "]";
	}
}