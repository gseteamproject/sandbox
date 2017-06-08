package knowledge.processor;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import knowledge.Knowledge;

public class ListenBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 3401188230499296662L;

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		if (message != null) {
			KnowledgeProcessorAgent myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;

			String conversationId = message.getConversationId();
			if (conversationId.equals(Knowledge.KNOWLEDGE_PRODUCE_FACT)) {
				myKnowledgeProcessorAgent.storeFact(message);
			} else if (conversationId.equals(Knowledge.KNOWLEDGE_CONSUME_FACT)) {
				myKnowledgeProcessorAgent.answerQuestion(message);
			} else {
				myKnowledgeProcessorAgent.notUnderStood(message);
			}
		} else {
			block();
		}
	}
}
