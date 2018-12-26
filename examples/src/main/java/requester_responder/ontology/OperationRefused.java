package requester_responder.ontology;

import jade.content.Predicate;

public class OperationRefused implements Predicate {

	private Operation operation;

	public Operation getOperation() {
		return this.operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	private String reason;

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	private static final long serialVersionUID = 2754728452941365696L;
}
