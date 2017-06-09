package knowledge.consumer;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import knowledge.KnowledgeAgent;

public class FindFactBehaviour extends AchieveREInitiator {

	private static final long serialVersionUID = -8559347099939136940L;

	public FindFactBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		KnowledgeAgent myKnowledgeAgent = (KnowledgeAgent) myAgent;
		myKnowledgeAgent.trace("knowledge processor liked my question");
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		KnowledgeAgent myKnowledgeAgent = (KnowledgeAgent) myAgent;
		myKnowledgeAgent.trace("knowledge processor does not liked my question");
	}
}
