package palleteRobotCommunication;

import jade.content.Predicate;

public class State implements Predicate {

	private static final long serialVersionUID = 2369398955920060920L;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
