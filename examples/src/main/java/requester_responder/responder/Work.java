package requester_responder.responder;

import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import requester_responder.models.Machine;
import requester_responder.models.Vocabulary;
import requester_responder.ontology.Operation;
import requester_responder.ontology.OperationCompleted;

public class Work extends SimpleBehaviour {

	private Activity owner = null;

	public Work(Activity ownerActivity) {
		this.owner = ownerActivity;
	}

	@Override
	public void action() {
		Machine machine = owner.getMachine();
		machine.execute();

		ACLMessage inform = owner.getRequest().createReply();
		inform.setPerformative(ACLMessage.INFORM);
		Operation operation = new Operation();
		operation.setName(Vocabulary.OPERATION_1_NAME);
		OperationCompleted completed = new OperationCompleted();
		completed.setOperation(operation);
		completed.setDuration(machine.getDuration());

		fillContent(inform, completed);

		owner.setResult(inform);
	}

	private void fillContent(ACLMessage message, Predicate predicate) {
		try {
			myAgent.getContentManager().fillContent(message, predicate);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean done() {
		return owner.getMachine().executed;
	}

	private static final long serialVersionUID = -7007875180021231044L;
}
