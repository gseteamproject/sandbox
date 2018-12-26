package requester_responder.responder;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import requester_responder.models.Machine;
import requester_responder.models.Vocabulary;
import requester_responder.ontology.Execute;
import requester_responder.ontology.Operation;
import requester_responder.ontology.OperationProposal;
import requester_responder.ontology.OperationRefused;

public class Decision extends OneShotBehaviour {

	@Override
	public void action() {
		ActivityRespond parent = (ActivityRespond) getParent();
		ACLMessage request = (ACLMessage) getDataStore().get(parent.REQUEST_KEY);

		Execute execute = (Execute) extractContent(request);
		Operation operation = execute.getOperation();
		System.out.println(String.format("%s: requested operation: %s", myAgent.getLocalName(), operation.getName()));

		Machine machine = (Machine) getDataStore().get(Vocabulary.MACHINE_OBJECT_KEY);

		ACLMessage response = request.createReply();
		if (machine.willExecute()) {
			response.setPerformative(ACLMessage.AGREE);
			OperationProposal proposal = new OperationProposal();
			proposal.setOperation(operation);
			proposal.setDurationEstimated(machine.getDurationEstimated());

			fillContent(response, proposal);

			Behaviour b = new Activity(myAgent, request);
			b.setDataStore(getDataStore());
			myAgent.addBehaviour(b);
		} else {
			response.setPerformative(ACLMessage.REFUSE);
			OperationRefused refused = new OperationRefused();
			refused.setOperation(operation);
			refused.setReason(machine.getReason());

			fillContent(response, refused);
		}

		getDataStore().put(parent.RESPONSE_KEY, response);
	}

	private AgentAction extractContent(ACLMessage message) {
		ContentManager cm = myAgent.getContentManager();
		Action a = null;
		try {
			a = (Action) cm.extractContent(message);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
		return (AgentAction) a.getAction();
	}

	private void fillContent(ACLMessage message, Predicate predicate) {
		try {
			myAgent.getContentManager().fillContent(message, predicate);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 8554746504981566315L;
}
