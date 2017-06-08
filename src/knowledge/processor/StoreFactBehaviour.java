package knowledge.processor;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class StoreFactBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -2485692838213609172L;

	private ACLMessage message;

	public StoreFactBehaviour(ACLMessage message) {
		this.message = message;
	}

	@Override
	public void action() {
		KnowledgeProcessorAgent myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;
		ACLMessage reply = message.createReply();

		if (message.getContent() == null) {
			myKnowledgeProcessorAgent.trace("incorrect fact (" + message.getContent() + ")");
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent("empty fact");
			myKnowledgeProcessorAgent.send(reply);
			return;
		}
		String strings[] = message.getContent().split("=");
		if (strings.length < 2) {
			myKnowledgeProcessorAgent.trace("incorrect fact (" + message.getContent() + ")");
			reply.setPerformative(ACLMessage.FAILURE);
			reply.setContent("incorrect fact");
			myKnowledgeProcessorAgent.send(reply);
			return;
		}

		String key = strings[0].trim();
		String value = strings[1].trim();
		myKnowledgeProcessorAgent.knowledge.put(key, value);
		myKnowledgeProcessorAgent.trace("stored fact ( key [" + key + "] value [" + value + "] )");
		reply.setPerformative(ACLMessage.INFORM);
		myKnowledgeProcessorAgent.send(reply);
	}
}
