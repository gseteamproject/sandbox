package requester_responder.ontology;

import jade.content.Predicate;

public class OperationCompleted implements Predicate {

	private Operation operation;

	public Operation getOperation() {
		return this.operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	private int duration;

	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	private static final long serialVersionUID = 503055190710323800L;
}
