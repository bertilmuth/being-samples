package org.requirementsascode.being.samples.greeting.model;

import java.util.Objects;

public final class GreetingState {
	public final String id;
	public final String salutation;
	public final String personName;

	public static GreetingState identifiedBy(final String id) {
		return new GreetingState(id, "", "");
	}

	public GreetingState(final String id, final String salutation, final String personName) {
		this.id = id;
		this.salutation = salutation;
		this.personName = personName;
	}

	@Override
	public String toString() {
		return "GreetingState [id=" + id + ", salutation=" + salutation + ", personName=" + personName + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, personName, salutation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GreetingState other = (GreetingState) obj;
		return Objects.equals(id, other.id) && Objects.equals(personName, other.personName)
			&& Objects.equals(salutation, other.salutation);
	}
	
	
}
