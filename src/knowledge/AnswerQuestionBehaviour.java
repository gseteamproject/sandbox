package knowledge;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AnswerQuestionBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -6512614753440637766L;

	private KnowledgeProcessorAgent myKnowledgeProcessorAgent;

	@Override
	public void onStart() {
		super.onStart();
		myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;
	}

	private ACLMessage message;

	public AnswerQuestionBehaviour(ACLMessage message) {
		this.message = message;
	}

	@Override
	public void action() {
		if (message.getContent() == null) {
			myKnowledgeProcessorAgent.trace("incorrect question (" + message.getContent() + ")");
			return;
		}
		String key = message.getContent().trim();
		String value = myKnowledgeProcessorAgent.knowledge.get(key);
		myKnowledgeProcessorAgent.trace("answer question ( key [" + key + "] value [" + value + "] )");
	}
}
