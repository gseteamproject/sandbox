package knowledge.producer;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import knowledge.KnowledgeAgent;

public class ProduceFactBehaviour extends AchieveREInitiator {

	private static final long serialVersionUID = -4202261991202665752L;

	public ProduceFactBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		KnowledgeAgent myKnowledgeAgent = (KnowledgeAgent) myAgent;
		myKnowledgeAgent.trace("knowledge processor liked my fact");
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		KnowledgeAgent myKnowledgeAgent = (KnowledgeAgent) myAgent;
		myKnowledgeAgent.trace("knowledge processor does not liked my fact");
	}
}
