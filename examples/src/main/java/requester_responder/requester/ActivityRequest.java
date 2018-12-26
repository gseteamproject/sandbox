package requester_responder.requester;

import java.util.Vector;

import jade.content.AgentAction;
import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import requester_responder.models.Vocabulary;
import requester_responder.ontology.Execute;
import requester_responder.ontology.Operation;
import requester_responder.ontology.OperationCompleted;
import requester_responder.ontology.OperationFailed;
import requester_responder.ontology.OperationProposal;
import requester_responder.ontology.OperationRefused;
import requester_responder.ontology.ScenarioOntology;

public class ActivityRequest extends AchieveREInitiator {

	public ActivityRequest(Agent a) {
		super(a, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Vector prepareRequests(ACLMessage message) {
		message = new ACLMessage(ACLMessage.REQUEST);
		message.addReceiver(new AID((Vocabulary.RESPONDER_AGENT_NAME), AID.ISLOCALNAME));
		message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

		Operation operation = new Operation();
		operation.setName(Vocabulary.OPERATION_1_NAME);
		Execute execute = new Execute();
		execute.setOperation(operation);

		fillContent(message, execute);

		Vector l = new Vector(1);
		l.addElement(message);
		return l;
	}

	@Override
	protected void handleAgree(ACLMessage message) {
		OperationProposal operationProposal = (OperationProposal) extractContent(message);
		System.out.println(String.format("%s: %s agreed (estimated time: %d)", myAgent.getLocalName(),
				message.getSender().getLocalName(), operationProposal.getDurationEstimated()));
	}

	@Override
	protected void handleRefuse(ACLMessage message) {
		OperationRefused operationRefused = (OperationRefused) extractContent(message);
		System.out.println(String.format("%s: %s refused (reason: %s)", myAgent.getLocalName(),
				message.getSender().getLocalName(), operationRefused.getReason()));
	}

	@Override
	protected void handleInform(ACLMessage message) {
		OperationCompleted operationCompleted = (OperationCompleted) extractContent(message);
		System.out.println(String.format("%s: %s finished (time: %d)", myAgent.getLocalName(),
				message.getSender().getLocalName(), operationCompleted.getDuration()));
	}

	@Override
	protected void handleFailure(ACLMessage message) {
		OperationFailed operationFailed = (OperationFailed) extractContent(message);
		System.out.println(String.format("%s: %s failed (reason: %s)", myAgent.getLocalName(),
				message.getSender().getLocalName(), operationFailed.getReason()));
	}

	private Predicate extractContent(ACLMessage message) {
		ContentManager cm = myAgent.getContentManager();
		Predicate p = null;
		try {
			p = (Predicate) cm.extractContent(message);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
		return p;
	}

	private void fillContent(ACLMessage message, AgentAction agentAction) {
		Action action = new Action();
		action.setActor((AID) message.getAllReceiver().next());
		action.setAction(agentAction);
		message.setLanguage(XMLCodec.NAME);
		message.setOntology(ScenarioOntology.ONTOLOGY_NAME);
		try {
			myAgent.getContentManager().fillContent(message, action);
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 1991081910743783420L;
}
