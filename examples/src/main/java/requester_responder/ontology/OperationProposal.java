package requester_responder.ontology;

import jade.content.Predicate;

public class OperationProposal implements Predicate {

	private Operation operation;

	public Operation getOperation() {
		return this.operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	private int durationEstimated;

	public int getDurationEstimated() {
		return this.durationEstimated;
	}

	public void setDurationEstimated(int durationEstimated) {
		this.durationEstimated = durationEstimated;
	}

	private static final long serialVersionUID = 8397127817695450356L;
}
