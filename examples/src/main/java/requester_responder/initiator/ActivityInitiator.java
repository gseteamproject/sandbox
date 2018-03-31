package requester_responder.initiator;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import requester_responder.Vocabulary;

public class ActivityInitiator extends AchieveREInitiator {

	public ActivityInitiator(Agent a) {
		super(a, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Vector prepareRequests(ACLMessage request) {
		request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(new AID((Vocabulary.RESPONDER_AGENT_NAME), AID.ISLOCALNAME));
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.setContent(Vocabulary.ACTIVITY_NAME);

		Vector l = new Vector(1);
		l.addElement(request);
		return l;
	}

	@Override
	protected void handleAgree(ACLMessage agree) {
		System.out.println(String.format("%s: agreed (estimated time: %s)", agree.getSender().getLocalName(),
				agree.getContent()));
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println(
				String.format("%s: refused (reason: %s)", refuse.getSender().getLocalName(), refuse.getContent()));
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println(
				String.format("%s: finished (time: %s)", inform.getSender().getLocalName(), inform.getContent()));
	}

	@Override
	protected void handleFailure(ACLMessage failure) {
		System.out.println(
				String.format("%s: failed (reason: %s)", failure.getSender().getLocalName(), failure.getContent()));
	}

	private static final long serialVersionUID = 1991081910743783420L;
}
