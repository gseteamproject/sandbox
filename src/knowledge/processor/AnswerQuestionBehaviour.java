package knowledge.processor;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AnswerQuestionBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -6512614753440637766L;

	private ACLMessage message;

	public AnswerQuestionBehaviour(ACLMessage message) {
		this.message = message;
	}

	@Override
	public void action() {
		KnowledgeProcessorAgent myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;
		ACLMessage reply = message.createReply();

		if (message.getContent() == null) {
			myKnowledgeProcessorAgent.trace("incorrect question (" + message.getContent() + ")");
			reply.setContent("emty question");
			reply.setPerformative(ACLMessage.FAILURE);
			myKnowledgeProcessorAgent.send(reply);
			return;
		}
		String key = message.getContent().trim();
		String value = myKnowledgeProcessorAgent.knowledge.get(key);
		myKnowledgeProcessorAgent.trace("answer question ( key [" + key + "] value [" + value + "] )");
		reply.setContent(value);
		reply.setPerformative(ACLMessage.INFORM);
		myKnowledgeProcessorAgent.send(reply);
	}
}
