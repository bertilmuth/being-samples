package org.requirementsascode.being.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.requirementsascode.being.model.greeting.GreetingState;

@SuppressWarnings("all")
public class GreetingData {
	public final String id;
	public final String salutation;
	public final String personName;
	public final String greetingText;

	public static GreetingData fromState(final GreetingState greetingState) {
		return from(greetingState.id, greetingState.salutation, greetingState.personName, greetingState.greetingText);
	}

	public static GreetingData from(final String id, final String salutation, final String personName, final String greetingText) {
		return new GreetingData(id, salutation, personName, greetingText);
	}

	public static List<GreetingData> fromAll(final List<GreetingState> states) {
		return states.stream().map(GreetingData::fromState).collect(Collectors.toList());
	}

	public static GreetingData empty() {
		return fromState(GreetingState.identifiedBy(""));
	}

	private GreetingData(final String id, final String salutation, String personName, String greetingText) {
		this.id = id;
		this.salutation = salutation;
		this.personName = personName;
		this.greetingText = greetingText;
	}

	public GreetingState toGreetingState() {
		return new GreetingState(id, salutation, personName);
	}


	@Override
	public String toString() {
		return "GreetingData [id=" + id + ", salutation=" + salutation + ", personName=" + personName
				+ ", greetingText=" + greetingText + "]";
	}
}
