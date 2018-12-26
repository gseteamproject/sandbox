package requester_responder.ontology;

import jade.content.AgentAction;

public class Execute implements AgentAction {

	private Operation operation;

	public Operation getOperation() {
		return this.operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	private static final long serialVersionUID = -8863488543888001686L;
}
