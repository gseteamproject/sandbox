package knowledge.processor;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class AnswerQuestionBehaviour extends AchieveREResponder {

	private static final long serialVersionUID = -6512614753440637766L;

	public AnswerQuestionBehaviour(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request) {
		KnowledgeProcessorAgent myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;
		ACLMessage reply = request.createReply();

		if (request.getContent() == null) {
			myKnowledgeProcessorAgent.trace("incorrect question (" + request.getContent() + ")");
			reply.setContent("emty question");
			reply.setPerformative(ACLMessage.REFUSE);
			myKnowledgeProcessorAgent.send(reply);
			return reply;
		}
		String key = request.getContent().trim();
		String value = myKnowledgeProcessorAgent.knowledge.get(key);
		myKnowledgeProcessorAgent.trace("answer question ( key [" + key + "] value [" + value + "] )");
		reply.setContent(value);
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}
}
