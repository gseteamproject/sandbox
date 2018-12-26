package requester_responder.responder;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREResponder;

public class ActivityRespond extends AchieveREResponder {

	public ActivityRespond(Agent a) {
		super(a, AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST));

		registerHandleRequest(new Decision());
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
		return null;
	}

	private static final long serialVersionUID = 4274732602764953111L;
}
