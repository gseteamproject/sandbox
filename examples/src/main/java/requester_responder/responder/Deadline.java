package requester_responder.responder;

import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import requester_responder.models.Machine;
import requester_responder.models.Vocabulary;
import requester_responder.ontology.Operation;
import requester_responder.ontology.OperationFailed;

public class Deadline extends WakerBehaviour {

	public Deadline(Agent a, long timeout) {
		super(a, timeout);
	}

	@Override
	protected void onWake() {
		Activity parent = (Activity) getParent();

		Machine machine = parent.getMachine();
		machine.terminate();

		ACLMessage inform = parent.getRequest().createReply();
		inform.setPerformative(ACLMessage.FAILURE);
		Operation operation = new Operation();
		operation.setName(Vocabulary.OPERATION_1_NAME);
		OperationFailed failed = new OperationFailed();
		failed.setOperation(operation);
		failed.setReason(machine.getReason());

		fillContent(inform, failed);

		parent.setResult(inform);
	}

	private void fillContent(ACLMessage message, Predicate predicate) {
		try {
			myAgent.getContentManager().fillContent(message, predicate);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 7276757247489520051L;
}
